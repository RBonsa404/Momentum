import { AfterViewInit, Component, ElementRef, OnDestroy, ViewChild } from '@angular/core';
import * as THREE from 'three';

@Component({
  selector: 'ui-halo-background',
  standalone: true,
  template: `<canvas #canvas class="pointer-events-none fixed inset-0 -z-10 h-full w-full"></canvas>`
})
export class HaloBackgroundComponent implements AfterViewInit, OnDestroy {
  @ViewChild('canvas', { static: true }) canvas!: ElementRef<HTMLCanvasElement>;
  private renderer?: THREE.WebGLRenderer;
  private frame = 0;

  ngAfterViewInit(): void {
    const canvas = this.canvas.nativeElement;
    const renderer = new THREE.WebGLRenderer({ canvas, antialias: true, alpha: true });
    this.renderer = renderer;
    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
    renderer.setSize(window.innerWidth, window.innerHeight);

    const scene = new THREE.Scene();
    const camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 0.1, 100);
    camera.position.z = 6;

    const geometry = new THREE.TorusGeometry(1.8, 0.55, 32, 120);
    const material = new THREE.MeshBasicMaterial({
      color: 0xff4500,
      transparent: true,
      opacity: 0.18,
      wireframe: true
    });
    const torus = new THREE.Mesh(geometry, material);
    scene.add(torus);

    const glow = new THREE.Mesh(
      new THREE.SphereGeometry(1.2, 48, 48),
      new THREE.MeshBasicMaterial({ color: 0xff6b35, transparent: true, opacity: 0.12 })
    );
    scene.add(glow);

    const particles = new THREE.BufferGeometry();
    const count = 400;
    const positions = new Float32Array(count * 3);
    for (let i = 0; i < count * 3; i++) {
      positions[i] = (Math.random() - 0.5) * 12;
    }
    particles.setAttribute('position', new THREE.BufferAttribute(positions, 3));
    const points = new THREE.Points(
      particles,
      new THREE.PointsMaterial({ color: 0xff6b35, size: 0.03, transparent: true, opacity: 0.45 })
    );
    scene.add(points);

    const animate = () => {
      this.frame = requestAnimationFrame(animate);
      torus.rotation.z += 0.0018;
      torus.rotation.x += 0.0006;
      glow.scale.setScalar(1 + Math.sin(Date.now() / 1400) * 0.08);
      points.rotation.y += 0.0004;
      renderer.render(scene, camera);
    };
    animate();

    window.addEventListener('resize', () => {
      camera.aspect = window.innerWidth / window.innerHeight;
      camera.updateProjectionMatrix();
      renderer.setSize(window.innerWidth, window.innerHeight);
    });
  }

  ngOnDestroy(): void {
    cancelAnimationFrame(this.frame);
    this.renderer?.dispose();
  }
}
