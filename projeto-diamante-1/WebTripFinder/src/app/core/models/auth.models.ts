export interface LoginRequest {
  email: string;
  password: string;
} 

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string | null;
  type: string;
  userId: number;
  name: string;
  email: string;
  role: string;
}

export interface UserResponse {
  id: number;
  name: string;
  email: string;
  imageUrl?: string | null;
  role: string;
}

export interface UpdateUserRequest {
  name?: string;
  email?: string;
  imageUrl?: string | null;
  currentPassword: string;
  newPassword?: string;
}
