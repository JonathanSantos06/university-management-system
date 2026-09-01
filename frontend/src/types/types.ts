export interface Student {
  id: string;
  userId: string;
  studentCode: string;
  careerId: string;
  admissionPeriodId: string;
  currentSemester: number;
  status: string;

  personalData: PersonalData;

  addresses: Address[];

  emergencyContacts: EmergencyContact[];

  createdAt: string;
  updatedAt: string;
}

export interface PersonalData {
  firstName: string;
  lastNamePaternal: string;
  lastNameMaternal: string;
  birthDate: string;
  gender: string;
  curp: string;
  rfc: string;
  nationality: string;
  phone: string;
  personalEmail: string;
}

export interface Address {
  id: string;
  addressType: string;
  street: string;
  extNumber: string;
  intNumber: string;
  neighborhood: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
}

export interface EmergencyContact {
  id: string;
  fullName: string;
  relationship: string;
  phone: string;
  email: string;
}


export interface LoginRequest {
  username: string;
  password: string;
}

export interface User {
  id: string;
  username: string;
  email: string;
  active: boolean;
  locked: boolean;
  roles: string[];
  lastLoginAt?: string;
  createdAt?: string;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  user: User;
}

