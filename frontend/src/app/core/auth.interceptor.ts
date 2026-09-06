import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthStore } from './auth.store';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthStore);
  const token = auth.accessToken();
  const cid = crypto.randomUUID();
  const headers: Record<string, string> = { 'X-Correlation-Id': cid };
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  const cloned = req.clone({ setHeaders: headers });
  return next(cloned).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status !== 401 || req.url.includes('/api/auth/')) {
        return throwError(() => err);
      }
      return auth.refresh().pipe(
        switchMap(() =>
          next(
            req.clone({
              setHeaders: {
                Authorization: `Bearer ${auth.accessToken() ?? ''}`,
                'X-Correlation-Id': cid
              }
            })
          )
        )
      );
    })
  );
};
