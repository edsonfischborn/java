const key = 'user';

export default class UserStorage {
  static store(data) {
    localStorage.setItem(key, JSON.stringify(data));
  }

  static get() {
    return JSON.parse(localStorage.getItem(key));
  }

  static clear() {
    localStorage.removeItem(key);
  }

  static validate() {
    const user = this.get();

    return user && user.id && user.name && user.email && user.cpf;
  }
}
