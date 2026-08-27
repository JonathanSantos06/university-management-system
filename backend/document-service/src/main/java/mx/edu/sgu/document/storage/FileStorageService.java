package mx.edu.sgu.document.storage;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.document.config.StorageProperties;
import mx.edu.sgu.document.exception.BusinessRuleException;
import mx.edu.sgu.document.exception.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Almacenamiento de archivos en el filesystem local. En docker-compose la ruta base
 * (sgu.storage.base-path=/app/storage) está montada sobre un volumen nombrado para que
 * los archivos sobrevivan a la recreación del contenedor.
 */
@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final StorageProperties storageProperties;

    public record StoredFile(String relativePath, String mimeType) {
    }

    public StoredFile store(UUID studentId, MultipartFile file) {
        validate(file);

        String originalName = sanitize(file.getOriginalFilename());
        String storedName = UUID.randomUUID() + "-" + originalName;
        Path studentDir = basePath().resolve(studentId.toString());

        try {
            Files.createDirectories(studentDir);
            Path target = studentDir.resolve(storedName);
            file.transferTo(target);
            String relativePath = studentId + "/" + storedName;
            return new StoredFile(relativePath, file.getContentType());
        } catch (IOException e) {
            throw new BusinessRuleException("No se pudo guardar el archivo: " + e.getMessage());
        }
    }

    public Resource loadAsResource(String relativePath) {
        try {
            Path file = basePath().resolve(relativePath).normalize();
            if (!file.startsWith(basePath())) {
                throw new BusinessRuleException("Ruta de archivo inválida");
            }
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("El archivo ya no está disponible en el almacenamiento");
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("El archivo ya no está disponible en el almacenamiento");
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessRuleException("El archivo está vacío o no fue enviado");
        }
        String contentType = file.getContentType();
        if (contentType == null || !storageProperties.allowedContentTypes().contains(contentType)) {
            throw new BusinessRuleException("Tipo de archivo no permitido: " + contentType
                    + ". Permitidos: " + String.join(", ", storageProperties.allowedContentTypes()));
        }
    }

    private Path basePath() {
        Path path = Paths.get(storageProperties.basePath()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new BusinessRuleException("No se pudo preparar el almacenamiento de documentos");
        }
        return path;
    }

    private String sanitize(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "archivo";
        }
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
