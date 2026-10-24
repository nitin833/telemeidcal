import { useCallback } from 'react';
import { useSelector } from 'react-redux';
import { selectToken } from '../store/authSlice';
import { apiFetch } from '../api';

export function useApi() {
  const token = useSelector(selectToken);

  const callApi = useCallback(async (path, options = {}) => {
    return await apiFetch(path, { ...options, token });
  }, [token]);

  return callApi;
}
