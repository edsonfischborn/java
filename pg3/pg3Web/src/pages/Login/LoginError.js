export default class LoginError extends Error {
  constructor(message, data) {
    super(message);

    this.message = message;
    this.data = data;
  }
}
