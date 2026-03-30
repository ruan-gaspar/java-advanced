import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { UserResponse } from '../../../../core/models/auth.models';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './profile.component.html'
})
export class ProfileComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);

  loading = signal(true);
  saving = signal(false);
  savingPhoto = signal(false);
  successMessage = signal('');
  errorMessage = signal('');
  user = signal<UserResponse | null>(null);
  previewImage = signal<string | null>(null);
  selectedFile = signal<File | null>(null);

  form = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    currentPassword: ['', [Validators.required]],
    newPassword: ['', [Validators.minLength(6)]]
  });

  profileImage = computed(() => {
    if (this.previewImage()) {
      return this.previewImage();
    }

    const currentUser = this.user();
    if (currentUser?.imageUrl) {
      return currentUser.imageUrl;
    }

    return 'https://placehold.co/160x160/0f172a/ffffff?text=Perfil';
  });

  constructor() {
    this.loadProfile();
  }

  loadProfile(): void {
    this.loading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.authService.me().subscribe({
      next: (response: UserResponse) => {
        this.user.set(response);
        this.previewImage.set(null);
        this.selectedFile.set(null);

        this.form.patchValue({
          name: response.name,
          email: response.email,
          currentPassword: '',
          newPassword: ''
        });

        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Erro ao carregar perfil.');
        this.loading.set(false);
      }
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) {
      return;
    }

    if (!file.type.startsWith('image/')) {
      this.errorMessage.set('Selecione um arquivo de imagem válido.');
      input.value = '';
      return;
    }

    this.selectedFile.set(file);
    this.errorMessage.set('');
    this.successMessage.set('');

    const reader = new FileReader();
    reader.onload = () => {
      this.previewImage.set(reader.result as string);
    };
    reader.readAsDataURL(file);

    input.value = '';
  }

  savePhoto(): void {
    const file = this.selectedFile();

    if (!file) {
      return;
    }

    this.savingPhoto.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.authService.uploadProfilePhoto(file).subscribe({
      next: (response: UserResponse) => {
        this.user.set(response);
        this.previewImage.set(null);
        this.selectedFile.set(null);
        this.successMessage.set('Foto de perfil atualizada com sucesso.');
        this.savingPhoto.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage.set(this.getErrorMessage(err));
        this.savingPhoto.set(false);
      }
    });
  }

  cancelPhotoSelection(): void {
    this.selectedFile.set(null);
    this.previewImage.set(null);
    this.errorMessage.set('');
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();

    this.saving.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.authService.updateMe({
      name: value.name?.trim() ?? '',
      email: value.email?.trim() ?? '',
      currentPassword: value.currentPassword ?? '',
      newPassword: value.newPassword?.trim() ?? ''
    }).subscribe({
      next: (response: UserResponse) => {
        this.user.set({
          ...response,
          imageUrl: response.imageUrl ?? this.user()?.imageUrl ?? null
        });

        this.form.patchValue({
          name: response.name,
          email: response.email,
          currentPassword: '',
          newPassword: ''
        });

        this.successMessage.set('Perfil atualizado com sucesso.');
        this.saving.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage.set(this.getErrorMessage(err));
        this.saving.set(false);
      }
    });
  }

  private getErrorMessage(err: HttpErrorResponse): string {
    const backendMessage = err.error?.message;

    if (backendMessage) {
      return backendMessage;
    }

    if (err.status === 0) {
      return 'Não foi possível conectar ao servidor.';
    }

    if (err.status === 400) {
      return 'Dados inválidos. Revise os campos informados.';
    }

    if (err.status === 401) {
      return 'Sua sessão expirou. Faça login novamente.';
    }

    if (err.status === 403) {
      return 'Você não tem permissão para esta ação.';
    }

    if (err.status === 404) {
      return 'Usuário não encontrado.';
    }

    if (err.status >= 500) {
      return 'Erro interno do servidor.';
    }

    return 'Não foi possível concluir a operação.';
  }
}
