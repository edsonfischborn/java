import { validate, getData } from '../util/jwtUtil';

const key = 'Authorization';

export default class TokenUtil {
  static store(token) {
    localStorage.setItem(key, token);
  }

  static get() {
    return localStorage.getItem(key);
  }

  static clear() {
    localStorage.removeItem(key);
  }

  static getData() {
    return getData(this.get());
  }

  static validate() {
    return validate(this.get());
  }
}
