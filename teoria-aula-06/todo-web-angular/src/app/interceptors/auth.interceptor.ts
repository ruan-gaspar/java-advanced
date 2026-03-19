import { HttpEvent, HttpHandlerFn, HttpRequest } from "@angular/common/http";
import { inject, Inject } from "@angular/core";
import { Observable } from "rxjs";
import { AuthService } from "../service/auth.service";

export function authInterceptor(request: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
  const authService = inject(AuthService);
  const token = authService.getToken();

  if(!token) return next(request);

  const reqWithHeader = request.clone({
    headers: request.headers.set('Authorization', `Bearer ${token}`),
  });

  return next(reqWithHeader);

}
