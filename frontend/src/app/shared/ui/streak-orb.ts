import { AfterViewInit, Component, ElementRef, Input, OnChanges, OnDestroy, SimpleChanges, ViewChild } from '@angular/core';
import * as THREE from 'three';

@Component({
  selector: 'ui-streak-orb',
  standalone: true,
  template: `
    <canvas #canvas class="h-[320px] w-full" [style.display]="webglFailed ? 'none' : 'block'"></canvas>
    @if (webglFailed) {
      <div class="h-[320px] w-full flex flex-col items-center justify-center gap-3">
        <div class="orb-fallback" [style.--intensity]="orbIntensity">
          <span class="orb-number">{{ length }}</span>
        </div>
        <p class="text-xs text-mute">jours de série</p>
      </div>
    }
  `,
  styles: [`
    .orb-fallback {
      width: 140px;
      height: 140px;
      border-radius: 9999px;
      background: radial-gradient(circle at 35% 35%, #ff6b35, #ff4500 60%, #7f2200);
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 0 calc(40px * var(--intensity, 0.3)) rgba(255,69,0,calc(0.4 * var(--intensity, 0.3))),
                  0 0 calc(80px * var(--intensity, 0.3)) rgba(255,69,0,calc(0.2 * var(--intensity, 0.3)));
      animation: orb-pulse 3s ease-in-out infinite;
    }
    .orb-number {
      font-size: 2.5rem;
      font-weight: 900;
      color: #fff;
      letter-spacing: -0.04em;
    }
    @keyframes orb-pulse {
      0%, 100% { transform: scale(1); }
      50% { transform: scale(1.06); }
    }
  `]
})
export class StreakOrbComponent implements AfterViewInit, OnChanges, OnDestroy {
  @ViewChild('canvas', { static: true }) canvas!: ElementRef<HTMLCanvasElement>;
  @Input() length = 0;
  webglFailed = false;
  orbIntensity = 0.3;

  private renderer?: THREE.WebGLRenderer;
  private frame = 0;
  private glowMat?: THREE.MeshBasicMaterial;
  private rings: THREE.Mesh[] = [];

  ngAfterViewInit(): void {
    try {
      // Vérifie si WebGL est disponible avant d'instancier Three.js
      const testCanvas = document.createElement('canvas');
      const gl = testCanvas.getContext('webgl') || testCanvas.getContext('experimental-webgl');
      if (!gl) {
        this.webglFailed = true;
        return;
      }

      const canvas = this.canvas.nativeElement;
      const renderer = new THREE.WebGLRenderer({ canvas, antialias: true, alpha: true });
      this.renderer = renderer;
      const w = canvas.clientWidth || 480;
      const h = canvas.clientHeight || 320;
      renderer.setSize(w, h, false);
      renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));

      const scene = new THREE.Scene();
      const camera = new THREE.PerspectiveCamera(42, w / h, 0.1, 50);
      camera.position.z = 5;

      const core = new THREE.Mesh(
        new THREE.SphereGeometry(0.7, 48, 48),
        new THREE.MeshBasicMaterial({ color: 0xff6b35 })
      );
      scene.add(core);

      this.glowMat = new THREE.MeshBasicMaterial({ color: 0xff4500, transparent: true, opacity: 0.2 });
      const glow = new THREE.Mesh(new THREE.SphereGeometry(1.15, 32, 32), this.glowMat);
      scene.add(glow);

      for (let i = 0; i < 3; i++) {
        const ring = new THREE.Mesh(
          new THREE.TorusGeometry(1.3 + i * 0.28, 0.03, 16, 80),
          new THREE.MeshBasicMaterial({ color: 0xff4500, transparent: true, opacity: 0.35 + i * 0.1 })
        );
        ring.rotation.x = Math.PI / 2.6 + i * 0.3;
        this.rings.push(ring);
        scene.add(ring);
      }

      this.applyIntensity();
      const animate = () => {
        this.frame = requestAnimationFrame(animate);
        core.rotation.y += 0.01;
        this.rings.forEach((r, i) => {
          r.rotation.z += 0.004 + i * 0.001;
        });
        glow.scale.setScalar(1 + Math.sin(Date.now() / 500) * 0.06);
        renderer.render(scene, camera);
      };
      animate();
    } catch {
      // WebGL non supporté ou erreur d'initialisation → fallback CSS
      this.webglFailed = true;
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.orbIntensity = Math.min(1, this.length / 30);
    if (changes['length'] && this.glowMat) {
      this.applyIntensity();
    }
  }

  ngOnDestroy(): void {
    cancelAnimationFrame(this.frame);
    this.renderer?.dispose();
  }

  private applyIntensity(): void {
    const intensity = Math.min(1, this.length / 30);
    if (this.glowMat) {
      this.glowMat.opacity = 0.15 + intensity * 0.55;
    }
    this.rings.forEach((ring, i) => {
      const mat = ring.material as THREE.MeshBasicMaterial;
      mat.opacity = 0.25 + intensity * 0.5 + i * 0.08;
    });
  }
}
