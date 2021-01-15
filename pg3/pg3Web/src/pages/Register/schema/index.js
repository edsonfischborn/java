import * as yup from 'yup';

export default yup.object().shape({
  name: yup
    .string()
    .min(7, 'O nome deve ter pelo menos 7 caracteres')
    .max(50, 'O nome deve ter no maximo 50 caracteres')
    .required('O nome deve ser informado'),
  email: yup.string().email().required('O e-mail deve ser informado'),
  password: yup
    .string()
    .min(6, 'A senha deve ter ao menos 6 caracteres')
    .max(15, 'A senha deve ter no maximo 15 caracteres')
    .required('A senha deve ser informada'),
  cpf: yup
    .number()
    .transform((value) => {
      if (isNaN(value)) return undefined;
      else return String(value).length === 11 ? value : undefined;
    })
    .required('O cpf deve conter 11 números'),
  phone: yup
    .number()
    .transform((value) => {
      if (isNaN(value)) return undefined;
      else return String(value).length === 11 ? value : undefined;
    })
    .required('O celular deve conter 11 números'),
});
