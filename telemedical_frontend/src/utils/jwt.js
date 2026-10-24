export function parseJwt(token) {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join('')
    );
    return JSON.parse(jsonPayload);
  } catch (e) {
    return null;
  }
}

export function extractUserFromToken(token) {
  const payload = parseJwt(token);
  if (!payload) return null;
  // Usually Spring Security puts username in "sub"
  // And authorities in "authorities" (or "roles")
  const role = payload.authorities ? payload.authorities[0] : null;
  // We need ID for making API calls. Sometimes ID is in "sub" or a custom claim like "userId"
  // If not present, we will fallback to username
  const userId = payload.userId || payload.id || payload.sub;
  return {
    id: userId,
    username: payload.sub,
    role: role // ROLE_ADMIN, ROLE_OWNER, ROLE_DOCTOR, ROLE_USER etc
  };
}
