import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HaloBackgroundComponent } from '../../shared/ui/halo-background';
import { AuthStore } from '../../core/auth.store';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, HaloBackgroundComponent],
  template: `
    <ui-halo-background />
    <div class="flex min-h-screen items-center justify-center px-4">
      <form class="glass glow w-full max-w-md p-10 text-center" (ngSubmit)="submit()">
        <p class="mb-2 text-xs tracking-[0.35em] text-mute">MOMENTUM</p>
        <h1 class="hero-title mb-6">
          {{ isRegister() ? 'Crée ton compte' : 'Entre dans ta journée' }}
        </h1>

        <!-- Mode Selector Tabs -->
        <div class="mb-6 flex justify-center gap-2 rounded-full border border-white/10 bg-white/5 p-1">
          <button
            type="button"
            class="w-1/2 rounded-full py-2 text-xs font-semibold transition-all"
            [class.bg-white-10]="!isRegister()"
            [class.text-white]="!isRegister()"
            [class.text-mute]="isRegister()"
            (click)="setMode(false)">
            Connexion
          </button>
          <button
            type="button"
            class="w-1/2 rounded-full py-2 text-xs font-semibold transition-all"
            [class.bg-white-10]="isRegister()"
            [class.text-white]="isRegister()"
            [class.text-mute]="!isRegister()"
            (click)="setMode(true)">
            Créer un compte
          </button>
        </div>

        @if (isRegister()) {
          <input
            class="mb-3 w-full rounded-full border border-white/10 bg-white/5 px-5 py-3 text-sm outline-none transitionFocus"
            [(ngModel)]="displayName"
            name="displayName"
            type="text"
            placeholder="Nom ou pseudo"
            required />
        }

        <input
          class="mb-3 w-full rounded-full border border-white/10 bg-white/5 px-5 py-3 text-sm outline-none transitionFocus"
          [(ngModel)]="email"
          name="email"
          type="email"
          placeholder="Email"
          required />

        <input
          class="mb-6 w-full rounded-full border border-white/10 bg-white/5 px-5 py-3 text-sm outline-none transitionFocus"
          [(ngModel)]="password"
          name="password"
          type="password"
          placeholder="Mot de passe"
          required />

        @if (error()) {
          <p class="mb-4 text-sm text-ember">{{ error() }}</p>
        }

        <button class="accent-grad glow w-full rounded-full py-3 text-sm font-semibold text-white cursor-pointer" type="submit">
          {{ isRegister() ? 'Créer mon compte' : 'Se connecter' }}
        </button>
      </form>
    </div>
  `
})
export class LoginPage {
  isRegister = signal(false);
  displayName = '';
  email = 'you@momentum.local';
  password = 'ChangeMeNow!';
  readonly error = signal('');

  constructor(
    private readonly auth: AuthStore,
    private readonly router: Router
  ) {}

  setMode(registerMode: boolean): void {
    this.isRegister.set(registerMode);
    this.error.set('');
  }

  submit(): void {
    this.error.set('');

    if (this.isRegister()) {
      if (!this.displayName.trim()) {
        this.error.set('Veuillez entrer un nom ou pseudo.');
        return;
      }
      this.auth.register(this.email, this.password, this.displayName).subscribe({
        next: () => void this.router.navigateByUrl('/'),
        error: (err) => {
          if (err.status === 409) {
            this.error.set('Cet email est déjà utilisé.');
          } else if (err.status === 400) {
            this.error.set('Email valide et mot de passe de 6 caractères minimum requis.');
          } else if (err.status === 0) {
            this.error.set('Le serveur backend est éteint. Lancez "docker compose up" dans backend/momentum-server.');
          } else {
            this.error.set('Erreur lors de la création du compte (Code ' + (err.status || 'inconnu') + ').');
          }
        }
      });
    } else {
      this.auth.login(this.email, this.password).subscribe({
        next: () => void this.router.navigateByUrl('/'),
        error: (err) => {
          if (err.status === 0) {
            this.error.set('Le serveur backend est éteint. Lancez "docker compose up" dans backend/momentum-server.');
          } else {
            this.error.set('Identifiants invalides ou compte verrouillé.');
          }
        }
      });
    }
  }
}
