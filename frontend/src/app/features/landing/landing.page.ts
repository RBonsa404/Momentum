import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HaloBackgroundComponent } from '../../shared/ui/halo-background';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [RouterLink, HaloBackgroundComponent],
  template: `
    <ui-halo-background />

    <!-- Navbar -->
    <header class="lp-nav">
      <div class="lp-nav-left">
        <img src="assets/favicon.png" alt="Momentum" class="lp-nav-fav" />
        <span class="lp-nav-brand">MOMENTUM</span>
      </div>
      <nav class="lp-nav-links">
        <a (click)="scrollTo('features')" class="lp-nav-link">Fonctionnalités</a>
        <a (click)="scrollTo('method')" class="lp-nav-link">La Méthode</a>
        <a (click)="scrollTo('faq')" class="lp-nav-link">FAQ</a>
      </nav>
      <div class="lp-nav-actions">
        <a routerLink="/login" class="btn-ghost">Connexion</a>
        <a routerLink="/login" [queryParams]="{ mode: 'register' }" class="btn-primary">
          Créer un compte
        </a>
      </div>
    </header>

    <!-- Hero -->
    <section class="lp-hero">
      <div class="lp-hero-badge">
        <span class="lp-badge-dot"></span>
        Plateforme de gestion du temps &amp; de discipline
      </div>

      <h1 class="lp-hero-title">
        Organisez votre journée.<br />
        <span class="lp-hero-accent">Exécutez sans distraction.</span>
      </h1>

      <p class="lp-hero-sub">
        Momentum est un espace de travail personnel conçu pour lier l'agenda par blocs de temps,
        le suivi de séries (streaks), le journal du soir et l'analyse de votre régularité.
      </p>

      <div class="lp-hero-actions">
        <a routerLink="/login" [queryParams]="{ mode: 'register' }" class="btn-primary btn-lg">
          Accéder à l'application →
        </a>
        <a (click)="scrollTo('features')" class="btn-ghost btn-lg lp-link-btn">
          En savoir plus
        </a>
      </div>

      <!-- App Preview Card — masqué sur très petit écran -->
      <div class="lp-preview">
        <div class="lp-preview-bar">
          <div class="lp-preview-dots">
            <span class="dot-r"></span>
            <span class="dot-y"></span>
            <span class="dot-g"></span>
            <span class="lp-preview-url">app.momentum.internal</span>
          </div>
          <span class="lp-preview-tag">Vue d'ensemble</span>
        </div>

        <div class="lp-stats">
          <div class="lp-stat">
            <p class="lp-stat-label">Taux d'accomplissement</p>
            <p class="lp-stat-val lp-stat-white">88 %</p>
          </div>
          <div class="lp-stat">
            <p class="lp-stat-label">Série en cours</p>
            <p class="lp-stat-val lp-stat-ember">14 jours</p>
          </div>
          <div class="lp-stat">
            <p class="lp-stat-label">Procrastination</p>
            <p class="lp-stat-val lp-stat-green">12 %</p>
          </div>
        </div>

        <div class="lp-plan">
          <p class="lp-plan-head">Planning du jour</p>
          <div class="lp-plan-row lp-plan-done">
            <span>09:00 – 11:30 · Session Deep Work</span>
            <span class="lp-plan-badge badge-done">Validé</span>
          </div>
          <div class="lp-plan-row lp-plan-prog">
            <span>14:00 – 16:00 · Révision des objectifs</span>
            <span class="lp-plan-badge badge-prog">En cours</span>
          </div>
        </div>
      </div>
    </section>

    <!-- Features -->
    <section id="features" class="lp-section">
      <div class="lp-section-head">
        <h2>Une suite d'outils conçue pour la clarté.</h2>
        <p>Chaque module répond à un besoin spécifique de votre workflow quotidien.</p>
      </div>

      <div class="lp-features-grid">
        @for (feat of features; track feat.num) {
          <div class="lp-feat-card" [class.lp-feat-soon]="feat.soon">
            <span class="lp-feat-num">{{ feat.num }}</span>
            <div class="lp-feat-head">
              <h3>{{ feat.title }}</h3>
              @if (feat.soon) {
                <span class="lp-feat-badge">Bientôt</span>
              }
            </div>
            <p>{{ feat.desc }}</p>
          </div>
        }
      </div>
    </section>

    <!-- Method -->
    <section id="method" class="lp-section">
      <div class="lp-method-card">
        <span class="lp-method-label">PRINCIPE FONDATEUR</span>
        <h2>La simplicité au service de la constance.</h2>
        <p>
          Momentum élimine les fonctionnalités superflues pour se concentrer sur l'essentiel :
          savoir précisément quoi faire à chaque moment de la journée, sans surcharge mentale.
        </p>
        <a routerLink="/login" [queryParams]="{ mode: 'register' }" class="btn-primary">
          Créer un compte gratuitement
        </a>
      </div>
    </section>

    <!-- FAQ -->
    <section id="faq" class="lp-section">
      <h2 class="lp-faq-title">Questions fréquentes</h2>
      <div class="lp-faq-list">
        @for (item of faqs; track item.q) {
          <div class="lp-faq-item">
            <h3>{{ item.q }}</h3>
            <p>{{ item.a }}</p>
          </div>
        }
      </div>
    </section>

    <!-- Footer -->
    <footer class="lp-footer">
      <div class="lp-footer-logo">
        <img src="assets/favicon.png" alt="Momentum" />
        <span>MOMENTUM</span>
      </div>
      <p>© 2026 Momentum. Tous droits réservés.</p>
      <div class="lp-footer-links">
        <a routerLink="/login">Connexion</a>
        <span>·</span>
        <a routerLink="/login" [queryParams]="{ mode: 'register' }">Inscription</a>
      </div>
    </footer>
  `,
  styles: [`
    /* ═══════════════════════════════════════════
       VARIABLES & TOKENS
    ═══════════════════════════════════════════ */
    :host {
      --ember: #ff4500;
      --flame: #ff6b35;
      --cream: #f5f5f5;
      --mute: #9ca3af;
      --glass-bg: rgba(255,255,255,0.04);
      --glass-border: rgba(255,255,255,0.08);
    }

    /* ═══════════════════════════════════════════
       NAVBAR
    ═══════════════════════════════════════════ */
    .lp-nav {
      position: sticky;
      top: 0;
      z-index: 40;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0.85rem 1.25rem;
      backdrop-filter: blur(20px);
      -webkit-backdrop-filter: blur(20px);
      background: rgba(0,0,0,0.55);
      border-bottom: 1px solid var(--glass-border);
      gap: 1rem;
    }
    .lp-nav-left {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      flex-shrink: 0;
    }
    .lp-nav-fav { width: 24px; height: 24px; border-radius: 5px; }
    .lp-nav-brand {
      font-family: monospace;
      font-size: 0.65rem;
      font-weight: 700;
      letter-spacing: 0.22em;
      color: var(--cream);
    }
    .lp-nav-links {
      display: none;
      gap: 1.75rem;
    }
    .lp-nav-link {
      font-size: 0.75rem;
      font-weight: 500;
      color: var(--mute);
      cursor: pointer;
      transition: color 0.15s;
    }
    .lp-nav-link:hover { color: var(--cream); }
    .lp-nav-actions {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      flex-shrink: 0;
    }
    @media (min-width: 640px) {
      .lp-nav { padding: 1rem 2rem; }
      .lp-nav-links { display: flex; }
    }

    /* ═══════════════════════════════════════════
       BUTTONS
    ═══════════════════════════════════════════ */
    .btn-primary {
      display: inline-flex;
      align-items: center;
      padding: 0.5rem 1rem;
      border-radius: 9999px;
      background: linear-gradient(135deg, var(--ember), var(--flame));
      color: #fff;
      font-size: 0.72rem;
      font-weight: 600;
      box-shadow: 0 0 50px rgba(255,69,0,0.2);
      transition: opacity 0.15s, transform 0.1s;
      white-space: nowrap;
    }
    .btn-primary:active { transform: scale(0.97); }
    .btn-ghost {
      display: inline-flex;
      align-items: center;
      padding: 0.5rem 1rem;
      border-radius: 9999px;
      border: 1px solid var(--glass-border);
      color: var(--cream);
      font-size: 0.72rem;
      font-weight: 500;
      transition: background 0.15s;
      white-space: nowrap;
    }
    .btn-ghost:hover { background: rgba(255,255,255,0.05); }
    .btn-lg {
      padding: 0.85rem 1.75rem;
      font-size: 0.85rem;
    }
    .lp-link-btn { cursor: pointer; }
    @media (max-width: 480px) {
      .btn-primary, .btn-ghost { font-size: 0.65rem; padding: 0.45rem 0.75rem; }
      .btn-lg { padding: 0.85rem 1.25rem; font-size: 0.8rem; }
    }

    /* ═══════════════════════════════════════════
       HERO
    ═══════════════════════════════════════════ */
    .lp-hero {
      max-width: 64rem;
      margin: 0 auto;
      padding: 3.5rem 1.25rem 3rem;
      text-align: center;
    }
    .lp-hero-badge {
      display: inline-flex;
      align-items: center;
      gap: 0.5rem;
      font-size: 0.7rem;
      color: var(--mute);
      border: 1px solid var(--glass-border);
      background: var(--glass-bg);
      border-radius: 9999px;
      padding: 0.35rem 0.85rem;
      margin-bottom: 1.5rem;
    }
    .lp-badge-dot {
      display: inline-block;
      width: 6px; height: 6px;
      border-radius: 9999px;
      background: var(--ember);
      animation: badge-pulse 2s ease-in-out infinite;
      flex-shrink: 0;
    }
    @keyframes badge-pulse {
      0%, 100% { opacity: 1; transform: scale(1); }
      50%       { opacity: 0.5; transform: scale(1.5); }
    }
    .lp-hero-title {
      font-size: clamp(2rem, 7vw, 4rem);
      font-weight: 900;
      line-height: 1.08;
      letter-spacing: -0.04em;
      color: var(--cream);
      margin-bottom: 1.25rem;
    }
    .lp-hero-accent {
      background: linear-gradient(135deg, var(--ember), var(--flame), #fbbf24);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }
    .lp-hero-sub {
      font-size: clamp(0.8rem, 2.5vw, 1rem);
      color: var(--mute);
      line-height: 1.7;
      max-width: 40rem;
      margin: 0 auto 2rem;
    }
    .lp-hero-actions {
      display: flex;
      flex-wrap: wrap;
      justify-content: center;
      gap: 0.75rem;
      margin-bottom: 3rem;
    }
    @media (min-width: 640px) {
      .lp-hero { padding: 5rem 2rem 4rem; }
      .lp-hero-actions { margin-bottom: 4rem; }
    }

    /* ── App Preview ──────────────────────────── */
    .lp-preview {
      background: var(--glass-bg);
      backdrop-filter: blur(20px);
      border: 1px solid var(--glass-border);
      border-radius: 16px;
      padding: 1.25rem;
      text-align: left;
      max-width: 56rem;
      margin: 0 auto;
    }
    .lp-preview-bar {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-bottom: 0.85rem;
      border-bottom: 1px solid var(--glass-border);
      margin-bottom: 1.25rem;
    }
    .lp-preview-dots { display: flex; align-items: center; gap: 0.35rem; }
    .dot-r { width: 9px; height: 9px; border-radius: 9999px; background: rgba(239,68,68,0.65); }
    .dot-y { width: 9px; height: 9px; border-radius: 9999px; background: rgba(234,179,8,0.65); }
    .dot-g { width: 9px; height: 9px; border-radius: 9999px; background: rgba(34,197,94,0.65); }
    .lp-preview-url {
      font-family: monospace;
      font-size: 0.6rem;
      color: #6b7280;
      margin-left: 0.5rem;
      display: none;
    }
    .lp-preview-tag {
      font-family: monospace;
      font-size: 0.6rem;
      color: #6b7280;
      padding: 0.18rem 0.55rem;
      border-radius: 9999px;
      background: var(--glass-bg);
      border: 1px solid var(--glass-border);
    }
    .lp-stats {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 0.75rem;
      margin-bottom: 1rem;
    }
    .lp-stat {
      background: rgba(255,255,255,0.04);
      border: 1px solid var(--glass-border);
      border-radius: 10px;
      padding: 0.75rem;
    }
    .lp-stat-label {
      font-size: 0.6rem;
      color: #6b7280;
      margin-bottom: 0.2rem;
      line-height: 1.3;
    }
    .lp-stat-val { font-size: 1.3rem; font-weight: 700; }
    .lp-stat-white { color: var(--cream); }
    .lp-stat-ember { color: var(--ember); }
    .lp-stat-green { color: #4ade80; }
    .lp-plan {
      background: rgba(255,255,255,0.03);
      border: 1px solid var(--glass-border);
      border-radius: 10px;
      padding: 0.85rem;
    }
    .lp-plan-head {
      font-size: 0.6rem;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.1em;
      color: #6b7280;
      margin-bottom: 0.65rem;
    }
    .lp-plan-row {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0.55rem 0.75rem;
      border-radius: 7px;
      background: rgba(0,0,0,0.35);
      font-size: 0.65rem;
      color: var(--cream);
      margin-bottom: 0.4rem;
      gap: 0.5rem;
    }
    .lp-plan-row:last-child { margin-bottom: 0; }
    .lp-plan-done  { border-left: 2px solid var(--ember); }
    .lp-plan-prog  { border-left: 2px solid var(--flame); }
    .lp-plan-badge { font-size: 0.58rem; font-weight: 600; flex-shrink: 0; }
    .badge-done { color: #4ade80; }
    .badge-prog { color: #fbbf24; }
    @media (min-width: 640px) {
      .lp-preview { padding: 2rem; border-radius: 20px; }
      .lp-preview-url { display: inline; }
      .lp-stat-val { font-size: 1.5rem; }
      .lp-plan-row { font-size: 0.72rem; padding: 0.65rem 0.85rem; }
    }

    /* ═══════════════════════════════════════════
       SECTIONS COMMUNES
    ═══════════════════════════════════════════ */
    .lp-section {
      max-width: 80rem;
      margin: 0 auto;
      padding: 3.5rem 1.25rem;
      border-top: 1px solid var(--glass-border);
    }
    .lp-section-head {
      text-align: center;
      margin-bottom: 2.5rem;
    }
    .lp-section-head h2 {
      font-size: clamp(1.3rem, 4vw, 1.75rem);
      font-weight: 800;
      color: var(--cream);
      margin-bottom: 0.65rem;
    }
    .lp-section-head p {
      font-size: 0.82rem;
      color: var(--mute);
    }
    @media (min-width: 640px) {
      .lp-section { padding: 5rem 2rem; }
      .lp-section-head { margin-bottom: 3.5rem; }
    }

    /* ── Features Grid ────────────────────────── */
    .lp-features-grid {
      display: grid;
      grid-template-columns: 1fr;
      gap: 1rem;
    }
    @media (min-width: 480px) {
      .lp-features-grid { grid-template-columns: repeat(2, 1fr); }
    }
    @media (min-width: 900px) {
      .lp-features-grid { grid-template-columns: repeat(3, 1fr); gap: 1.25rem; }
    }
    .lp-feat-card {
      background: var(--glass-bg);
      border: 1px solid var(--glass-border);
      border-radius: 16px;
      padding: 1.25rem;
      transition: border-color 0.2s;
    }
    .lp-feat-card:hover { border-color: rgba(255,255,255,0.18); }
    .lp-feat-card.lp-feat-soon {
      border-color: rgba(255,69,0,0.2);
      background: rgba(255,69,0,0.04);
    }
    .lp-feat-num {
      display: block;
      font-family: monospace;
      font-size: 0.6rem;
      font-weight: 700;
      letter-spacing: 0.18em;
      color: rgba(255,255,255,0.18);
      margin-bottom: 0.85rem;
    }
    .lp-feat-head {
      display: flex;
      align-items: flex-start;
      justify-content: space-between;
      gap: 0.5rem;
      margin-bottom: 0.55rem;
    }
    .lp-feat-head h3 {
      font-size: 0.82rem;
      font-weight: 700;
      color: var(--cream);
    }
    .lp-feat-badge {
      font-size: 0.55rem;
      font-weight: 700;
      text-transform: uppercase;
      letter-spacing: 0.08em;
      padding: 0.18rem 0.5rem;
      border-radius: 9999px;
      background: rgba(255,69,0,0.15);
      color: var(--ember);
      border: 1px solid rgba(255,69,0,0.3);
      white-space: nowrap;
      flex-shrink: 0;
    }
    .lp-feat-card p {
      font-size: 0.72rem;
      color: var(--mute);
      line-height: 1.6;
    }

    /* ── Method Card ──────────────────────────── */
    .lp-method-card {
      background: var(--glass-bg);
      border: 1px solid var(--glass-border);
      border-radius: 18px;
      padding: 2rem 1.5rem;
      text-align: center;
    }
    .lp-method-label {
      display: block;
      font-family: monospace;
      font-size: 0.6rem;
      font-weight: 700;
      letter-spacing: 0.22em;
      color: var(--ember);
      margin-bottom: 1rem;
    }
    .lp-method-card h2 {
      font-size: clamp(1.2rem, 4vw, 1.75rem);
      font-weight: 800;
      color: var(--cream);
      margin-bottom: 1rem;
    }
    .lp-method-card p {
      font-size: 0.82rem;
      color: var(--mute);
      line-height: 1.7;
      max-width: 36rem;
      margin: 0 auto 1.75rem;
    }
    @media (min-width: 640px) {
      .lp-method-card { padding: 3.5rem; }
    }

    /* ── FAQ ──────────────────────────────────── */
    .lp-faq-title {
      font-size: clamp(1.2rem, 4vw, 1.5rem);
      font-weight: 700;
      color: var(--cream);
      text-align: center;
      margin-bottom: 2rem;
    }
    .lp-faq-list { display: flex; flex-direction: column; gap: 0.75rem; }
    .lp-faq-item {
      background: var(--glass-bg);
      border: 1px solid var(--glass-border);
      border-radius: 12px;
      padding: 1.1rem 1.25rem;
    }
    .lp-faq-item h3 {
      font-size: 0.82rem;
      font-weight: 700;
      color: var(--cream);
      margin-bottom: 0.45rem;
    }
    .lp-faq-item p {
      font-size: 0.72rem;
      color: var(--mute);
      line-height: 1.65;
    }

    /* ── Footer ───────────────────────────────── */
    .lp-footer {
      border-top: 1px solid var(--glass-border);
      padding: 2rem 1.25rem;
      text-align: center;
    }
    .lp-footer-logo {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 0.5rem;
      margin-bottom: 0.75rem;
    }
    .lp-footer-logo img { width: 18px; height: 18px; border-radius: 4px; }
    .lp-footer-logo span {
      font-family: monospace;
      font-size: 0.6rem;
      font-weight: 700;
      letter-spacing: 0.22em;
      color: var(--cream);
    }
    .lp-footer p {
      font-size: 0.62rem;
      color: #4b5563;
      margin-bottom: 0.75rem;
    }
    .lp-footer-links {
      display: flex;
      justify-content: center;
      gap: 1rem;
      font-size: 0.62rem;
      color: #4b5563;
    }
    .lp-footer-links a { color: #6b7280; transition: color 0.15s; }
    .lp-footer-links a:hover { color: var(--cream); }
  `]
})
export class LandingPage {
  readonly features = [
    {
      num: '01',
      title: 'Time-Blocking',
      desc: 'Organisez votre journée en blocs horaires définis. Associez vos tâches aux fenêtres temporelles appropriées.',
      soon: false
    },
    {
      num: '02',
      title: 'Système de Streaks',
      desc: 'Mesurez votre continuité quotidienne. Un système de jokers paramétrables vous évite de casser une série sur un imprévu.',
      soon: false
    },
    {
      num: '03',
      title: 'Objectifs & Jalons',
      desc: 'Structurez vos grands projets en étapes claires et suivez la progression des habitudes associées.',
      soon: false
    },
    {
      num: '04',
      title: 'Statistiques & Heatmap',
      desc: 'Visualisez votre historique de complétion et suivez l\'évolution de votre score de report de tâches.',
      soon: false
    },
    {
      num: '05',
      title: 'Journal du soir',
      desc: 'Consignez vos réussites, vos difficultés et notez vos niveaux d\'énergie et d\'humeur en fin de journée.',
      soon: false
    },
    {
      num: '06',
      title: 'Application Mobile',
      desc: 'L\'application native iOS & Android est en cours de finalisation. La plateforme Web est déjà accessible sur smartphone.',
      soon: true
    }
  ];

  readonly faqs = [
    {
      q: 'L\'accès à Momentum est-il payant ?',
      a: 'Non, l\'accès à la plateforme Web est entièrement gratuit pour gérer votre agenda, vos séries d\'habitudes et votre journal.'
    },
    {
      q: 'Comment est calculé le score de procrastination ?',
      a: 'Le score est établi en comparant le volume de tâches planifiées et le nombre de tâches réellement clôturées ou décalées.'
    },
    {
      q: 'Quand l\'application mobile native sera-t-elle disponible ?',
      a: 'L\'application native iOS et Android est en développement. En attendant, Momentum est déjà accessible et utilisable depuis un navigateur mobile.'
    }
  ];

  scrollTo(id: string): void {
    const el = document.getElementById(id);
    if (el) {
      el.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }
}
