export const getData = (t) => {
  try {
    const [, token] = t.split(' ');
    return JSON.parse(atob(token.split('.')[1]));
  } catch (ex) {
    return null;
  }
};

export const validate = (token) => {
  const tokenData = getData(token);

  if (!tokenData) {
    return false;
  }

  const currentDate = Date.now().valueOf() / 1000;
  const { exp: expDate } = tokenData;

  if (expDate && currentDate < expDate) {
    return true;
  }

  return false;
};
