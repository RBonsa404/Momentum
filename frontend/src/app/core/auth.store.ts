import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

export interface TokenResponse {
  accessToken: string;
  refreshToken: string;
  expiresInSeconds: number;
  userId: string;
}

@Injectable({ providedIn: 'root' })
export class AuthStore {
  readonly accessToken = signal<string | null>(localStorage.getItem('momentum.access'));
  readonly userId = signal<string | null>(localStorage.getItem('momentum.userId'));

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router
  ) {}

  register(email: string, password: string, displayName: string) {
    return this.http.post<TokenResponse>('/api/auth/register', { email, password, displayName }).pipe(tap((t) => this.persist(t)));
  }

  login(email: string, password: string) {
    return this.http.post<TokenResponse>('/api/auth/login', { email, password }).pipe(tap((t) => this.persist(t)));
  }

  refresh(): Observable<TokenResponse> {
    const refreshToken = localStorage.getItem('momentum.refresh');
    return this.http.post<TokenResponse>('/api/auth/refresh', { refreshToken }).pipe(tap((t) => this.persist(t)));
  }

  logout() {
    localStorage.removeItem('momentum.access');
    localStorage.removeItem('momentum.refresh');
    localStorage.removeItem('momentum.userId');
    this.accessToken.set(null);
    this.userId.set(null);
    void this.router.navigateByUrl('/login');
  }

  private persist(tokens: TokenResponse) {
    localStorage.setItem('momentum.access', tokens.accessToken);
    localStorage.setItem('momentum.refresh', tokens.refreshToken);
    localStorage.setItem('momentum.userId', tokens.userId);
    this.accessToken.set(tokens.accessToken);
    this.userId.set(tokens.userId);
  }
}
