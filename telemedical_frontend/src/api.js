const API_BASE = process.env.REACT_APP_API_BASE || "http://localhost:8081";

/**
 * Login: POST /login with Basic Auth, returns JWT token string.
 * Adjust the endpoint / response shape to match your Spring backend.
 */
export async function login(username, password) {
  const basic = btoa(`${username}:${password}`);
  const res = await fetch(`${API_BASE}/login`, {
    method: "POST",
    headers: {
      "Authorization": `Basic ${basic}`,
      "Content-Type": "application/json",
    },
  });

  const text = await res.text();
  if (!res.ok) {
    throw new Error(text || `Login failed: HTTP ${res.status}`);
  }

  // Your Spring backend may return the JWT as:
  //   - a plain string body  → we use text directly
  //   - JSON like { token: "..." } → change to: JSON.parse(text).token
  // Adjust the line below to match your backend's actual response:
  let token;
  try {
    const json = JSON.parse(text);
    // Common shapes: { token }, { jwt }, { accessToken }
    token = json.token ?? json.jwt ?? json.accessToken ?? text;
  } catch {
    token = text; // plain-string JWT
  }

  if (!token) throw new Error("No token received from server");
  return token;
}

/**
 * Authenticated API call using a Bearer JWT token.
 */
export async function apiFetch(path, { method = "GET", body, token } = {}) {
  const headers = {
    "Content-Type": "application/json",
  };

  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const res = await fetch(`${API_BASE}${path}`, {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  });

  const text = await res.text();
  let data = null;
  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = text;
    }
  }

  if (!res.ok) {
    const msg = typeof data === "string" ? data : data?.message || `HTTP ${res.status}`;
    throw new Error(msg);
  }

  return data;
}