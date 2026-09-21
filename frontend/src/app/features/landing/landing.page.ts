import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HaloBackgroundComponent } from '../../shared/ui/halo-background';
import { FloatingBadgeComponent } from '../../shared/ui/floating-badge';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [RouterLink, HaloBackgroundComponent, FloatingBadgeComponent],
  template: `
    <ui-halo-background />

    <!-- Public Landing Header -->
    <header class="sticky top-0 z-40 flex items-center justify-between px-4 sm:px-8 py-4 backdrop-blur-xl bg-black/40 border-b border-white/5">
      <div class="flex items-center gap-3">
        <img src="assets/favicon.png" alt="Momentum Logo" class="h-8 w-8 rounded-lg shadow-lg" />
        <span class="text-sm font-black tracking-[0.3em] text-white">MOMENTUM</span>
      </div>
      <nav class="hidden md:flex items-center gap-6 text-xs font-medium text-mute">
        <a href="#features" class="hover:text-cream transition-colors">Fonctionnalités</a>
        <a href="#method" class="hover:text-cream transition-colors">La Méthode</a>
        <a href="#faq" class="hover:text-cream transition-colors">FAQ</a>
      </nav>
      <div class="flex items-center gap-2 sm:gap-3">
        <a
          routerLink="/login"
          class="rounded-full px-4 py-2 text-xs font-semibold text-cream border border-white/10 hover:border-white/20 bg-white/5 transition-all">
          Connexion
        </a>
        <a
          routerLink="/login"
          [queryParams]="{ mode: 'register' }"
          class="accent-grad glow rounded-full px-4 py-2 text-xs font-semibold text-white active:scale-95 transition-all">
          Rejoindre Momentum
        </a>
      </div>
    </header>

    <!-- Hero Section -->
    <section class="relative px-4 sm:px-6 pt-12 sm:pt-20 pb-16 text-center max-w-5xl mx-auto">
      <div class="mb-6 flex flex-wrap justify-center gap-2 sm:gap-3">
        <ui-floating-badge label="⚡ Time-Blocking" delay="0s" />
        <ui-floating-badge label="🔥 System Streak" delay="0.3s" />
        <ui-floating-badge label="📊 Score de Procrastination" delay="0.6s" />
      </div>

      <h1 class="text-3xl sm:text-5xl md:text-6xl font-extrabold tracking-tight leading-[1.1] mb-6">
        Dominez votre journée.<br />
        <span class="bg-gradient-to-r from-ember via-flame to-yellow-500 bg-clip-text text-transparent">
          Bâtissez une discipline d'acier.
        </span>
      </h1>

      <p class="text-sm sm:text-lg text-mute max-w-2xl mx-auto mb-8 leading-relaxed">
        Momentum réinvente votre organisation personnelle. Associez l'agenda par blocs de temps, le suivi d'habitudes SMART, le bilan du soir et l'analyse intelligente de votre procrastination.
      </p>

      <div class="flex flex-col sm:flex-row justify-center items-center gap-4 mb-16">
        <a
          routerLink="/login"
          [queryParams]="{ mode: 'register' }"
          class="accent-grad glow w-full sm:w-auto rounded-full px-8 py-4 text-sm font-bold text-white shadow-2xl active:scale-95 transition-all">
          Démarrer gratuitement ➔
        </a>
        <a
          href="#features"
          class="w-full sm:w-auto rounded-full px-6 py-4 text-sm font-semibold text-cream border border-white/10 hover:bg-white/5 transition-all">
          Découvrir le système
        </a>
      </div>

      <!-- App Interface Mockup Preview Card -->
      <div class="glass glow relative mx-auto max-w-4xl p-4 sm:p-8 rounded-3xl border border-white/10 text-left overflow-hidden">
        <div class="flex items-center justify-between pb-4 border-b border-white/10 mb-6">
          <div class="flex items-center gap-2">
            <span class="h-3 w-3 rounded-full bg-red-500/80"></span>
            <span class="h-3 w-3 rounded-full bg-yellow-500/80"></span>
            <span class="h-3 w-3 rounded-full bg-green-500/80"></span>
            <span class="text-xs text-mute ml-2 font-mono">dashboard.momentum.app</span>
          </div>
          <span class="text-xs px-3 py-1 rounded-full bg-ember/20 text-ember font-semibold">Live Preview</span>
        </div>

        <div class="grid gap-4 sm:grid-cols-3 mb-6">
          <div class="glass p-4 rounded-2xl">
            <p class="text-xs text-mute">Score de Procrastination</p>
            <p class="text-2xl font-black text-ember mt-1">12 % <span class="text-xs font-normal text-green-400">(-8% cette semaine)</span></p>
          </div>
          <div class="glass p-4 rounded-2xl">
            <p class="text-xs text-mute">Série Actuelle (Streak)</p>
            <p class="text-2xl font-black text-white mt-1">14 Jours 🔥 <span class="text-xs font-normal text-mute">(2 jokers restants)</span></p>
          </div>
          <div class="glass p-4 rounded-2xl">
            <p class="text-xs text-mute">Complétion Agenda</p>
            <p class="text-2xl font-black text-green-400 mt-1">88 % <span class="text-xs font-normal text-mute">(7/8 tâches)</span></p>
          </div>
        </div>

        <div class="glass p-4 rounded-2xl">
          <div class="flex items-center justify-between mb-3">
            <span class="text-xs font-semibold text-cream">Blocs de temps d'aujourd'hui</span>
            <span class="text-xs text-mute">08:00 - 18:00</span>
          </div>
          <div class="space-y-2">
            <div class="flex items-center justify-between rounded-xl bg-white/5 p-3 border-l-4 border-ember">
              <span class="text-xs font-medium text-white">09:00 - 11:30 · Deep Work Architecture & Code</span>
              <span class="text-xs font-bold text-green-400">Terminé ✓</span>
            </div>
            <div class="flex items-center justify-between rounded-xl bg-white/5 p-3 border-l-4 border-flame">
              <span class="text-xs font-medium text-white">14:00 - 16:00 · Rédaction & Stratégie Produit</span>
              <span class="text-xs font-bold text-yellow-400">En cours ⏳</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section id="features" class="px-4 sm:px-6 py-16 sm:py-24 max-w-6xl mx-auto">
      <div class="text-center mb-16">
        <h2 class="text-2xl sm:text-4xl font-extrabold mb-4">
          Un arsenal complet pour votre productivité.
        </h2>
        <p class="text-sm sm:text-base text-mute max-w-xl mx-auto">
          Conçu pour éliminer la dispersion mentale et vous concentrer sur ce qui déplace vraiment l'aiguille.
        </p>
      </div>

      <div class="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
        <!-- Feature 1 -->
        <div class="glass p-6 sm:p-8 rounded-3xl hover:border-ember/40 transition-all">
          <div class="h-12 w-12 rounded-2xl bg-ember/10 flex items-center justify-center text-2xl mb-5">📅</div>
          <h3 class="text-lg font-bold text-white mb-2">Agenda & Time-Blocking</h3>
          <p class="text-xs sm:text-sm text-mute leading-relaxed">
            Découpez vos journées en blocs de temps ciblés. Associez vos tâches précises et éliminez la fatigue décisionnelle.
          </p>
        </div>

        <!-- Feature 2 -->
        <div class="glass p-6 sm:p-8 rounded-3xl hover:border-ember/40 transition-all">
          <div class="h-12 w-12 rounded-2xl bg-ember/10 flex items-center justify-center text-2xl mb-5">🔥</div>
          <h3 class="text-lg font-bold text-white mb-2">Streak & Jokers Flexibles</h3>
          <p class="text-xs sm:text-sm text-mute leading-relaxed">
            Maintenez votre régularité quotidienne sans culpabiliser. Bénéficiez d'un système de jokers mensuels pour gérer les imprévus.
          </p>
        </div>

        <!-- Feature 3 -->
        <div class="glass p-6 sm:p-8 rounded-3xl hover:border-ember/40 transition-all">
          <div class="h-12 w-12 rounded-2xl bg-ember/10 flex items-center justify-center text-2xl mb-5">🎯</div>
          <h3 class="text-lg font-bold text-white mb-2">Objectifs SMART & Habitudes</h3>
          <p class="text-xs sm:text-sm text-mute leading-relaxed">
            Transformez vos grandes ambitions en objectifs SMART mesurables, découpés en jalons et habitudes quotidiennes.
          </p>
        </div>

        <!-- Feature 4 -->
        <div class="glass p-6 sm:p-8 rounded-3xl hover:border-ember/40 transition-all">
          <div class="h-12 w-12 rounded-2xl bg-ember/10 flex items-center justify-center text-2xl mb-5">📊</div>
          <h3 class="text-lg font-bold text-white mb-2">Score de Procrastination</h3>
          <p class="text-xs sm:text-sm text-mute leading-relaxed">
            Visualisez votre taux de réalisation en temps réel. Identifiez vos patterns de report de tâches et rectifiez votre trajectoire.
          </p>
        </div>

        <!-- Feature 5 -->
        <div class="glass p-6 sm:p-8 rounded-3xl hover:border-ember/40 transition-all">
          <div class="h-12 w-12 rounded-2xl bg-ember/10 flex items-center justify-center text-2xl mb-5">📓</div>
          <h3 class="text-lg font-bold text-white mb-2">Journal & Énergie</h3>
          <p class="text-xs sm:text-sm text-mute leading-relaxed">
            Clôturez vos journées avec le bilan du soir. Notez vos victoires, vos obstacles, votre gratitude et votre niveau d'énergie.
          </p>
        </div>

        <!-- Feature 6 -->
        <div class="glass p-6 sm:p-8 rounded-3xl hover:border-ember/40 transition-all">
          <div class="h-12 w-12 rounded-2xl bg-ember/10 flex items-center justify-center text-2xl mb-5">📱</div>
          <h3 class="text-lg font-bold text-white mb-2">100% Mobile & Sync</h3>
          <p class="text-xs sm:text-sm text-mute leading-relaxed">
            Accédez à vos données partout. Synchronisation instantanée entre votre ordinateur et votre application smartphone.
          </p>
        </div>
      </div>
    </section>

    <!-- Method Philosophy Section -->
    <section id="method" class="px-4 sm:px-6 py-16 max-w-5xl mx-auto">
      <div class="glass p-8 sm:p-12 rounded-3xl border border-white/10 text-center relative overflow-hidden">
        <p class="text-xs font-semibold tracking-[0.3em] text-ember mb-3">LA SAGESSE DU MOUNTING</p>
        <h2 class="text-2xl sm:text-4xl font-black mb-6">
          "La discipline n'est pas une punition. C'est la clé de votre liberté."
        </h2>
        <p class="text-xs sm:text-sm text-mute max-w-2xl mx-auto leading-relaxed mb-8">
          La plupart des to-do lists accumulent des tâches sans contexte temporel, créant un stress chronique. Momentum vous offre la clarté d'un emploi du temps structuré couplée à la motivation visuelle de vos séries.
        </p>
        <a
          routerLink="/login"
          [queryParams]="{ mode: 'register' }"
          class="accent-grad glow inline-block rounded-full px-8 py-3.5 text-sm font-bold text-white active:scale-95 transition-all">
          Créer mon espace maintenant ➔
        </a>
      </div>
    </section>

    <!-- FAQ Section -->
    <section id="faq" class="px-4 sm:px-6 py-16 max-w-4xl mx-auto">
      <h2 class="text-2xl sm:text-3xl font-extrabold text-center mb-12">Foire aux questions</h2>
      <div class="space-y-4">
        @for (item of faqs; track item.q) {
          <div class="glass p-6 rounded-2xl">
            <h3 class="text-sm sm:text-base font-bold text-white mb-2">{{ item.q }}</h3>
            <p class="text-xs sm:text-sm text-mute leading-relaxed">{{ item.a }}</p>
          </div>
        }
      </div>
    </section>

    <!-- Footer -->
    <footer class="border-t border-white/10 px-4 sm:px-8 py-8 text-center text-xs text-mute">
      <div class="flex items-center justify-center gap-2 mb-4">
        <img src="assets/favicon.png" alt="Momentum Logo" class="h-6 w-6 rounded-md" />
        <span class="font-bold text-white tracking-widest">MOMENTUM</span>
      </div>
      <p class="mb-2">© 2026 Momentum App. Tous droits réservés. Dominez votre journée.</p>
      <div class="flex justify-center gap-4 text-mute">
        <a routerLink="/login" class="hover:text-cream">Connexion</a>
        <span>·</span>
        <a routerLink="/login" [queryParams]="{ mode: 'register' }" class="hover:text-cream">Inscription</a>
      </div>
    </footer>
  `
})
export class LandingPage {
  readonly faqs = [
    {
      q: 'Momentum est-il gratuit ?',
      a: 'Oui, vous pouvez créer votre compte et utiliser l’ensemble des fonctionnalités de gestion d’agenda, d’habitudes et de journal gratuitement.'
    },
    {
      q: 'Comment fonctionne le score de procrastination ?',
      a: 'Momentum analyse la différence entre les tâches planifiées dans vos blocs de temps et celles réellement accomplies ou reportées pour calculer un score clair en pourcentage.'
    },
    {
      q: 'Puis-je installer Momentum sur mon téléphone ?',
      a: 'Absolument ! Momentum est entièrement responsive et peut être installé comme application mobile native grâce à Capacitor (iOS & Android).'
    }
  ];
}
