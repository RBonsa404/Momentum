import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HaloBackgroundComponent } from '../../shared/ui/halo-background';
import { AuthStore } from '../../core/auth.store';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink, HaloBackgroundComponent],
  template: `
    <ui-halo-background />
    <div class="flex min-h-screen items-center justify-center px-4 py-8 sm:py-12">
      <form class="glass glow w-full max-w-md p-6 sm:p-10 text-center relative" (ngSubmit)="submit()">
        <a routerLink="/welcome" class="absolute top-4 left-5 text-xs text-mute hover:text-cream transition-colors flex items-center gap-1">
          ← Retour
        </a>

        <p class="mb-2 text-xs tracking-[0.35em] text-mute uppercase mt-2 sm:mt-0">MOMENTUM</p>
        <h1 class="hero-title mb-6 text-2xl sm:text-4xl">
          {{ isRegister() ? 'Crée ton compte' : 'Entre dans ta journée' }}
        </h1>

        <!-- Mode Selector Tabs -->
        <div class="mb-6 flex justify-center gap-1 sm:gap-2 rounded-full border border-white/10 bg-white/5 p-1">
          <button
            type="button"
            class="w-1/2 rounded-full py-2 text-xs font-semibold transition-all cursor-pointer"
            [class.bg-white-10]="!isRegister()"
            [class.text-white]="!isRegister()"
            [class.text-mute]="isRegister()"
            (click)="setMode(false)">
            Connexion
          </button>
          <button
            type="button"
            class="w-1/2 rounded-full py-2 text-xs font-semibold transition-all cursor-pointer"
            [class.bg-white-10]="isRegister()"
            [class.text-white]="isRegister()"
            [class.text-mute]="!isRegister()"
            (click)="setMode(true)">
            Créer un compte
          </button>
        </div>

        @if (isRegister()) {
          <input
            class="mb-3 w-full rounded-full border border-white/10 bg-white/5 px-4 sm:px-5 py-3 text-sm outline-none transitionFocus"
            [(ngModel)]="displayName"
            name="displayName"
            type="text"
            placeholder="Nom ou pseudo"
            autocomplete="name"
            required />
        }

        <input
          class="mb-3 w-full rounded-full border border-white/10 bg-white/5 px-4 sm:px-5 py-3 text-sm outline-none transitionFocus"
          [(ngModel)]="email"
          name="email"
          type="email"
          placeholder="Email"
          autocomplete="email"
          required />

        <input
          class="mb-6 w-full rounded-full border border-white/10 bg-white/5 px-4 sm:px-5 py-3 text-sm outline-none transitionFocus"
          [(ngModel)]="password"
          name="password"
          type="password"
          placeholder="Mot de passe"
          autocomplete="current-password"
          required />

        @if (error()) {
          <p class="mb-4 text-xs sm:text-sm text-ember leading-relaxed">{{ error() }}</p>
        }

        <button class="accent-grad glow w-full rounded-full py-3.5 text-sm font-semibold text-white cursor-pointer active:scale-95 transition-transform mb-4" type="submit">
          {{ isRegister() ? 'Créer mon compte' : 'Se connecter' }}
        </button>

        <p class="text-xs text-mute">
          En continuant, vous acceptez les conditions d'utilisation de Momentum.
        </p>
      </form>
    </div>
  `
})
export class LoginPage implements OnInit {
  isRegister = signal(false);
  displayName = '';
  email = '';
  password = '';
  readonly error = signal('');

  constructor(
    private readonly auth: AuthStore,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      if (params['mode'] === 'register') {
        this.isRegister.set(true);
      }
    });
  }

  setMode(registerMode: boolean): void {
    this.isRegister.set(registerMode);
    this.error.set('');
  }

  submit(): void {
    this.error.set('');

    if (!this.email.trim() || !this.password) {
      this.error.set('Veuillez saisir votre email et votre mot de passe.');
      return;
    }

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
            this.error.set('Impossible de joindre le serveur. Vérifiez votre connexion internet.');
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
            this.error.set('Impossible de joindre le serveur. Vérifiez votre connexion internet.');
          } else {
            this.error.set('Identifiants invalides ou compte verrouillé.');
          }
        }
      });
    }
  }
}
