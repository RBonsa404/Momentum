import { gsap } from 'gsap';

export function cascadeIn(selector: string, scope?: Element): void {
  const root = scope ?? document;
  const items = root.querySelectorAll(selector);
  gsap.fromTo(
    items,
    { y: 24, opacity: 0 },
    { y: 0, opacity: 1, duration: 0.7, stagger: 0.08, ease: 'power3.out' }
  );
}

export function burst(x: number, y: number): void {
  const canvas = document.createElement('canvas');
  canvas.width = window.innerWidth;
  canvas.height = window.innerHeight;
  canvas.style.cssText = 'position:fixed;inset:0;pointer-events:none;z-index:50';
  document.body.appendChild(canvas);
  const ctx = canvas.getContext('2d');
  if (!ctx) {
    canvas.remove();
    return;
  }
  const particles = Array.from({ length: 18 }, () => ({
    x,
    y,
    vx: (Math.random() - 0.5) * 8,
    vy: (Math.random() - 0.5) * 8,
    life: 1
  }));
  const tick = () => {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    particles.forEach((p) => {
      p.x += p.vx;
      p.y += p.vy;
      p.life -= 0.04;
      ctx.fillStyle = `rgba(255,69,0,${Math.max(p.life, 0)})`;
      ctx.beginPath();
      ctx.arc(p.x, p.y, 3, 0, Math.PI * 2);
      ctx.fill();
    });
    if (particles.some((p) => p.life > 0)) {
      requestAnimationFrame(tick);
    } else {
      canvas.remove();
    }
  };
  tick();
}
