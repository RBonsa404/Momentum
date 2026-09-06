import { AfterViewInit, Component, ElementRef, Input, OnChanges, OnDestroy, SimpleChanges, ViewChild } from '@angular/core';
import * as THREE from 'three';

@Component({
  selector: 'ui-streak-orb',
  standalone: true,
  template: `<canvas #canvas class="h-[320px] w-full"></canvas>`
})
export class StreakOrbComponent implements AfterViewInit, OnChanges, OnDestroy {
  @ViewChild('canvas', { static: true }) canvas!: ElementRef<HTMLCanvasElement>;
  @Input() length = 0;
  private renderer?: THREE.WebGLRenderer;
  private frame = 0;
  private glowMat?: THREE.MeshBasicMaterial;
  private rings: THREE.Mesh[] = [];

  ngAfterViewInit(): void {
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
  }

  ngOnChanges(changes: SimpleChanges): void {
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
