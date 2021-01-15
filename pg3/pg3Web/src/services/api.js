import axios from 'axios';
import TokenStorage from './TokenStorage';

const api = axios.create({
  baseURL: 'http://192.168.0.105:8080',
  headers: {
    Authorization: TokenStorage.get(),
  },
});

export default api;
