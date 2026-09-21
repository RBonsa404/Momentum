import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HaloBackgroundComponent } from '../../shared/ui/halo-background';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [RouterLink, HaloBackgroundComponent],
  template: `
    <ui-halo-background />

    <!-- ══════════════════════════════════════════
         MOBILE — COMING SOON
    ══════════════════════════════════════════ -->
    <div class="mobile-soon">
      <div class="mobile-soon-inner">
        <div class="mobile-soon-logo">
          <img src="assets/favicon.png" alt="Momentum" class="mobile-soon-fav" />
          <span class="mobile-soon-brand">MOMENTUM</span>
        </div>

        <span class="mobile-soon-badge">
          <i class="mobile-soon-dot"></i>
          Version mobile en cours
        </span>

        <h1 class="mobile-soon-title">
          Bientôt sur<br />
          <span class="mobile-soon-accent">votre mobile.</span>
        </h1>

        <p class="mobile-soon-desc">
          L'interface mobile est en développement actif.
          Pour l'instant, ouvrez Momentum depuis un ordinateur.
        </p>

        <div class="mobile-soon-line"><span>En attendant</span></div>

        <a routerLink="/login" class="mobile-soon-cta">
          Accéder depuis Desktop →
        </a>

        <p class="mobile-soon-copy">© 2026 Momentum</p>
      </div>
    </div>

    <!-- ══════════════════════════════════════════
         DESKTOP — LANDING PAGE
    ══════════════════════════════════════════ -->
    <div class="desktop-wrap">

      <!-- Navbar -->
      <header class="nav">
        <div class="nav-left">
          <img src="assets/favicon.png" alt="Momentum" class="nav-fav" />
          <span class="nav-brand">MOMENTUM</span>
        </div>
        <nav class="nav-links">
          <a href="#features">Fonctionnalités</a>
          <a href="#method">La Méthode</a>
          <a href="#faq">FAQ</a>
        </nav>
        <div class="nav-actions">
          <a routerLink="/login" class="btn-ghost">Connexion</a>
          <a routerLink="/login" [queryParams]="{ mode: 'register' }" class="btn-primary">Créer un compte</a>
        </div>
      </header>

      <!-- Hero -->
      <section class="hero">
        <div class="hero-badge">
          <span class="hero-dot"></span>
          Plateforme de gestion du temps &amp; de discipline
        </div>

        <h1 class="hero-title">
          Organisez votre journée.<br />
          <span class="hero-accent">Exécutez sans distraction.</span>
        </h1>

        <p class="hero-sub">
          Momentum est un espace de travail personnel conçu pour lier l'agenda par blocs de temps,
          le suivi de séries (streaks), le journal du soir et l'analyse de votre régularité.
        </p>

        <div class="hero-actions">
          <a routerLink="/login" [queryParams]="{ mode: 'register' }" class="btn-primary btn-lg">
            Accéder à l'application →
          </a>
          <a href="#features" class="btn-ghost btn-lg">En savoir plus</a>
        </div>

        <!-- App Preview -->
        <div class="preview-card">
          <div class="preview-bar">
            <div class="preview-dots">
              <span class="dot-r"></span>
              <span class="dot-y"></span>
              <span class="dot-g"></span>
              <span class="preview-url">app.momentum.internal</span>
            </div>
            <span class="preview-tag">Vue d'ensemble</span>
          </div>

          <div class="preview-stats">
            <div class="stat-cell">
              <p class="stat-label">Taux d'accomplissement</p>
              <p class="stat-val stat-white">88 %</p>
            </div>
            <div class="stat-cell">
              <p class="stat-label">Série en cours</p>
              <p class="stat-val stat-ember">14 jours</p>
            </div>
            <div class="stat-cell">
              <p class="stat-label">Score de procrastination</p>
              <p class="stat-val stat-green">12 %</p>
            </div>
          </div>

          <div class="preview-plan">
            <p class="plan-head">Planning du jour</p>
            <div class="plan-row plan-done">
              <span>09:00 – 11:30 · Session Deep Work — Développement Core</span>
              <span class="plan-badge badge-done">Validé</span>
            </div>
            <div class="plan-row plan-inprog">
              <span>14:00 – 16:00 · Rédaction &amp; Révision des objectifs</span>
              <span class="plan-badge badge-inprog">En cours</span>
            </div>
          </div>
        </div>
      </section>

      <!-- Features -->
      <section id="features" class="features-section">
        <div class="section-head">
          <h2>Une suite d'outils conçue pour la clarté.</h2>
          <p>Chaque module répond à un besoin spécifique de votre workflow quotidien.</p>
        </div>

        <div class="features-grid">
          @for (feat of features; track feat.num) {
            <div class="feat-card" [class.feat-soon]="feat.soon">
              <span class="feat-index">{{ feat.num }}</span>
              <div class="feat-head">
                <h3>{{ feat.title }}</h3>
                @if (feat.soon) {
                  <span class="feat-badge">Bientôt</span>
                }
              </div>
              <p>{{ feat.desc }}</p>
            </div>
          }
        </div>
      </section>

      <!-- Method -->
      <section id="method" class="method-section">
        <div class="method-card">
          <span class="method-label">PRINCIPE FONDATEUR</span>
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
      <section id="faq" class="faq-section">
        <h2>Questions fréquentes</h2>
        <div class="faq-list">
          @for (item of faqs; track item.q) {
            <div class="faq-item">
              <h3>{{ item.q }}</h3>
              <p>{{ item.a }}</p>
            </div>
          }
        </div>
      </section>

      <!-- Footer -->
      <footer class="site-footer">
        <div class="footer-logo">
          <img src="assets/favicon.png" alt="Momentum" />
          <span>MOMENTUM</span>
        </div>
        <p>© 2026 Momentum. Tous droits réservés.</p>
        <div class="footer-links">
          <a routerLink="/login">Connexion</a>
          <span>·</span>
          <a routerLink="/login" [queryParams]="{ mode: 'register' }">Inscription</a>
        </div>
      </footer>

    </div>
  `,
  styles: [`
    /* ──────────────────────────────────────────────
       MOBILE COMING SOON  (visible < 768px)
    ────────────────────────────────────────────── */
    .mobile-soon {
      display: flex;
      align-items: center;
      justify-content: center;
      min-height: 100dvh;
      padding: 2rem 1.5rem;
      text-align: center;
    }
    .mobile-soon-inner {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 1.2rem;
      max-width: 340px;
      width: 100%;
    }
    .mobile-soon-logo {
      display: flex;
      align-items: center;
      gap: 0.6rem;
    }
    .mobile-soon-fav {
      width: 28px; height: 28px;
      border-radius: 6px;
    }
    .mobile-soon-brand {
      font-family: monospace;
      font-size: 0.7rem;
      font-weight: 700;
      letter-spacing: 0.25em;
      color: #f5f5f5;
    }
    .mobile-soon-badge {
      display: inline-flex;
      align-items: center;
      gap: 0.5rem;
      font-size: 0.7rem;
      color: #9ca3af;
      border: 1px solid rgba(255,255,255,0.1);
      background: rgba(255,255,255,0.04);
      border-radius: 9999px;
      padding: 0.35rem 0.85rem;
    }
    .mobile-soon-dot {
      display: inline-block;
      width: 6px; height: 6px;
      border-radius: 9999px;
      background: #ff4500;
      animation: pulse-dot 2s ease-in-out infinite;
    }
    @keyframes pulse-dot {
      0%, 100% { opacity: 1; transform: scale(1); }
      50%       { opacity: 0.5; transform: scale(1.5); }
    }
    .mobile-soon-title {
      font-size: 2.2rem;
      font-weight: 900;
      line-height: 1.1;
      letter-spacing: -0.03em;
      color: #f5f5f5;
      margin: 0;
    }
    .mobile-soon-accent {
      background: linear-gradient(135deg, #ff4500, #ff6b35, #fbbf24);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }
    .mobile-soon-desc {
      font-size: 0.8rem;
      color: #9ca3af;
      line-height: 1.7;
      margin: 0;
    }
    .mobile-soon-line {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      width: 100%;
      color: #4b5563;
      font-size: 0.62rem;
      letter-spacing: 0.12em;
      text-transform: uppercase;
    }
    .mobile-soon-line::before,
    .mobile-soon-line::after {
      content: '';
      flex: 1;
      height: 1px;
      background: rgba(255,255,255,0.08);
    }
    .mobile-soon-cta {
      display: block;
      width: 100%;
      padding: 0.9rem 1.5rem;
      border-radius: 9999px;
      background: linear-gradient(135deg, #ff4500, #ff6b35);
      color: #fff;
      font-size: 0.8rem;
      font-weight: 600;
      text-align: center;
      box-shadow: 0 0 40px rgba(255, 69, 0, 0.3);
      transition: opacity 0.2s;
    }
    .mobile-soon-cta:active { opacity: 0.8; }
    .mobile-soon-copy {
      font-size: 0.62rem;
      color: #374151;
      margin: 0;
    }

    /* ──────────────────────────────────────────────
       DESKTOP WRAPPER  (hidden on mobile)
    ────────────────────────────────────────────── */
    .desktop-wrap { display: none; }

    @media (min-width: 768px) {
      .mobile-soon { display: none; }
      .desktop-wrap { display: block; }
    }

    /* ── Navbar ───────────────────────────────────── */
    .nav {
      position: sticky;
      top: 0;
      z-index: 40;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 1rem 2rem;
      backdrop-filter: blur(20px);
      -webkit-backdrop-filter: blur(20px);
      background: rgba(0,0,0,0.5);
      border-bottom: 1px solid rgba(255,255,255,0.05);
    }
    .nav-left { display: flex; align-items: center; gap: 0.75rem; }
    .nav-fav { width: 28px; height: 28px; border-radius: 6px; }
    .nav-brand {
      font-family: monospace;
      font-size: 0.7rem;
      font-weight: 700;
      letter-spacing: 0.25em;
      color: #f5f5f5;
    }
    .nav-links { display: flex; gap: 2rem; }
    .nav-links a {
      font-size: 0.75rem;
      font-weight: 500;
      color: #9ca3af;
      transition: color 0.15s;
    }
    .nav-links a:hover { color: #f5f5f5; }
    .nav-actions { display: flex; align-items: center; gap: 0.75rem; }

    /* ── Buttons ──────────────────────────────────── */
    .btn-primary {
      display: inline-flex;
      align-items: center;
      padding: 0.55rem 1.25rem;
      border-radius: 9999px;
      background: linear-gradient(135deg, #ff4500, #ff6b35);
      color: #fff;
      font-size: 0.75rem;
      font-weight: 600;
      box-shadow: 0 0 60px rgba(255,69,0,0.2);
      transition: opacity 0.15s, transform 0.1s;
    }
    .btn-primary:active { transform: scale(0.97); }
    .btn-ghost {
      display: inline-flex;
      align-items: center;
      padding: 0.55rem 1.25rem;
      border-radius: 9999px;
      border: 1px solid rgba(255,255,255,0.1);
      color: #f5f5f5;
      font-size: 0.75rem;
      font-weight: 500;
      transition: background 0.15s;
    }
    .btn-ghost:hover { background: rgba(255,255,255,0.05); }
    .btn-lg { padding: 0.85rem 2rem; font-size: 0.85rem; }

    /* ── Hero ─────────────────────────────────────── */
    .hero {
      max-width: 64rem;
      margin: 0 auto;
      padding: 7rem 2rem 5rem;
      text-align: center;
    }
    .hero-badge {
      display: inline-flex;
      align-items: center;
      gap: 0.5rem;
      font-size: 0.72rem;
      color: #9ca3af;
      border: 1px solid rgba(255,255,255,0.1);
      background: rgba(255,255,255,0.04);
      border-radius: 9999px;
      padding: 0.35rem 0.85rem;
      margin-bottom: 1.5rem;
    }
    .hero-dot {
      display: inline-block;
      width: 7px; height: 7px;
      border-radius: 9999px;
      background: #ff4500;
      animation: pulse-dot 2s ease-in-out infinite;
    }
    .hero-title {
      font-size: clamp(2.5rem, 5vw, 4rem);
      font-weight: 900;
      line-height: 1.08;
      letter-spacing: -0.04em;
      color: #f5f5f5;
      margin-bottom: 1.25rem;
    }
    .hero-accent {
      background: linear-gradient(135deg, #ff4500, #ff6b35, #fbbf24);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }
    .hero-sub {
      font-size: 0.95rem;
      color: #9ca3af;
      line-height: 1.7;
      max-width: 40rem;
      margin: 0 auto 2.5rem;
    }
    .hero-actions {
      display: flex;
      justify-content: center;
      gap: 1rem;
      margin-bottom: 5rem;
    }

    /* ── App Preview Card ─────────────────────────── */
    .preview-card {
      background: rgba(255,255,255,0.04);
      backdrop-filter: blur(20px);
      border: 1px solid rgba(255,255,255,0.08);
      border-radius: 20px;
      padding: 2rem;
      text-align: left;
      max-width: 56rem;
      margin: 0 auto;
    }
    .preview-bar {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-bottom: 1rem;
      border-bottom: 1px solid rgba(255,255,255,0.08);
      margin-bottom: 1.5rem;
    }
    .preview-dots { display: flex; align-items: center; gap: 0.4rem; }
    .dot-r { width: 10px; height: 10px; border-radius: 9999px; background: rgba(239,68,68,0.7); }
    .dot-y { width: 10px; height: 10px; border-radius: 9999px; background: rgba(234,179,8,0.7); }
    .dot-g { width: 10px; height: 10px; border-radius: 9999px; background: rgba(34,197,94,0.7); }
    .preview-url {
      font-family: monospace;
      font-size: 0.68rem;
      color: #6b7280;
      margin-left: 0.6rem;
    }
    .preview-tag {
      font-family: monospace;
      font-size: 0.65rem;
      color: #6b7280;
      padding: 0.2rem 0.65rem;
      border-radius: 9999px;
      background: rgba(255,255,255,0.04);
      border: 1px solid rgba(255,255,255,0.08);
    }
    .preview-stats {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 1rem;
      margin-bottom: 1.5rem;
    }
    .stat-cell {
      background: rgba(255,255,255,0.04);
      border: 1px solid rgba(255,255,255,0.08);
      border-radius: 12px;
      padding: 1rem;
    }
    .stat-label { font-size: 0.68rem; color: #6b7280; margin-bottom: 0.25rem; }
    .stat-val { font-size: 1.5rem; font-weight: 700; }
    .stat-white { color: #f5f5f5; }
    .stat-ember { color: #ff4500; }
    .stat-green { color: #4ade80; }
    .preview-plan {
      background: rgba(255,255,255,0.04);
      border: 1px solid rgba(255,255,255,0.08);
      border-radius: 12px;
      padding: 1rem;
    }
    .plan-head {
      font-size: 0.65rem;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.1em;
      color: #6b7280;
      margin-bottom: 0.75rem;
    }
    .plan-row {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0.65rem 0.85rem;
      border-radius: 8px;
      background: rgba(0,0,0,0.35);
      font-size: 0.72rem;
      color: #f5f5f5;
      margin-bottom: 0.5rem;
    }
    .plan-row:last-child { margin-bottom: 0; }
    .plan-done  { border-left: 2px solid #ff4500; }
    .plan-inprog { border-left: 2px solid #ff6b35; }
    .plan-badge { font-size: 0.62rem; font-weight: 600; }
    .badge-done   { color: #4ade80; }
    .badge-inprog { color: #fbbf24; }

    /* ── Features Section ─────────────────────────── */
    .features-section {
      max-width: 80rem;
      margin: 0 auto;
      padding: 6rem 2rem;
      border-top: 1px solid rgba(255,255,255,0.05);
    }
    .section-head {
      text-align: center;
      margin-bottom: 4rem;
    }
    .section-head h2 {
      font-size: 1.75rem;
      font-weight: 800;
      color: #f5f5f5;
      margin-bottom: 0.75rem;
    }
    .section-head p {
      font-size: 0.85rem;
      color: #9ca3af;
    }
    .features-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 1.25rem;
    }
    .feat-card {
      background: rgba(255,255,255,0.04);
      border: 1px solid rgba(255,255,255,0.08);
      border-radius: 18px;
      padding: 1.5rem;
      transition: border-color 0.2s;
    }
    .feat-card:hover { border-color: rgba(255,255,255,0.18); }
    .feat-card.feat-soon {
      border-color: rgba(255,69,0,0.2);
      background: rgba(255,69,0,0.04);
    }
    .feat-index {
      display: block;
      font-family: monospace;
      font-size: 0.62rem;
      font-weight: 700;
      letter-spacing: 0.18em;
      color: rgba(255,255,255,0.18);
      margin-bottom: 0.85rem;
    }
    .feat-head {
      display: flex;
      align-items: flex-start;
      justify-content: space-between;
      gap: 0.5rem;
      margin-bottom: 0.6rem;
    }
    .feat-head h3 {
      font-size: 0.82rem;
      font-weight: 700;
      color: #f5f5f5;
    }
    .feat-badge {
      font-size: 0.58rem;
      font-weight: 700;
      text-transform: uppercase;
      letter-spacing: 0.08em;
      padding: 0.2rem 0.5rem;
      border-radius: 9999px;
      background: rgba(255,69,0,0.15);
      color: #ff4500;
      border: 1px solid rgba(255,69,0,0.3);
      white-space: nowrap;
      flex-shrink: 0;
    }
    .feat-card p {
      font-size: 0.72rem;
      color: #9ca3af;
      line-height: 1.6;
    }

    /* ── Method Section ───────────────────────────── */
    .method-section {
      max-width: 56rem;
      margin: 0 auto;
      padding: 4rem 2rem;
      border-top: 1px solid rgba(255,255,255,0.05);
    }
    .method-card {
      background: rgba(255,255,255,0.04);
      border: 1px solid rgba(255,255,255,0.08);
      border-radius: 20px;
      padding: 3.5rem;
      text-align: center;
    }
    .method-label {
      display: block;
      font-family: monospace;
      font-size: 0.62rem;
      font-weight: 700;
      letter-spacing: 0.25em;
      color: #ff4500;
      margin-bottom: 1rem;
    }
    .method-card h2 {
      font-size: 1.75rem;
      font-weight: 800;
      color: #f5f5f5;
      margin-bottom: 1rem;
    }
    .method-card p {
      font-size: 0.85rem;
      color: #9ca3af;
      line-height: 1.7;
      max-width: 36rem;
      margin: 0 auto 2rem;
    }

    /* ── FAQ Section ──────────────────────────────── */
    .faq-section {
      max-width: 56rem;
      margin: 0 auto;
      padding: 4rem 2rem;
      border-top: 1px solid rgba(255,255,255,0.05);
    }
    .faq-section h2 {
      font-size: 1.5rem;
      font-weight: 700;
      color: #f5f5f5;
      text-align: center;
      margin-bottom: 2.5rem;
    }
    .faq-list { display: flex; flex-direction: column; gap: 0.75rem; }
    .faq-item {
      background: rgba(255,255,255,0.04);
      border: 1px solid rgba(255,255,255,0.08);
      border-radius: 14px;
      padding: 1.25rem 1.5rem;
    }
    .faq-item h3 {
      font-size: 0.82rem;
      font-weight: 700;
      color: #f5f5f5;
      margin-bottom: 0.5rem;
    }
    .faq-item p {
      font-size: 0.72rem;
      color: #9ca3af;
      line-height: 1.65;
    }

    /* ── Footer ───────────────────────────────────── */
    .site-footer {
      border-top: 1px solid rgba(255,255,255,0.08);
      padding: 2rem;
      text-align: center;
    }
    .footer-logo {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 0.5rem;
      margin-bottom: 0.75rem;
    }
    .footer-logo img {
      width: 18px; height: 18px;
      border-radius: 4px;
    }
    .footer-logo span {
      font-family: monospace;
      font-size: 0.62rem;
      font-weight: 700;
      letter-spacing: 0.25em;
      color: #f5f5f5;
    }
    .site-footer p {
      font-size: 0.65rem;
      color: #4b5563;
      margin-bottom: 0.75rem;
    }
    .footer-links {
      display: flex;
      justify-content: center;
      gap: 1rem;
      font-size: 0.65rem;
      color: #4b5563;
    }
    .footer-links a {
      color: #6b7280;
      transition: color 0.15s;
    }
    .footer-links a:hover { color: #f5f5f5; }
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
      desc: 'L\'application native iOS & Android est en cours de finalisation. En attendant, la plateforme Web reste disponible.',
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
      a: 'Le score est établi en comparant le volume de tâches planifiées au cours de vos sessions et le nombre de tâches réellement clôturées ou décalées.'
    },
    {
      q: 'Quand l\'application mobile sera-t-elle disponible ?',
      a: 'L\'application native pour iOS et Android est actuellement en développement (Coming Soon). En attendant, le site Web est adapté pour une utilisation mobile.'
    }
  ];
}
