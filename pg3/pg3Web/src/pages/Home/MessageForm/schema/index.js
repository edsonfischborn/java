import * as yup from 'yup';

export default yup.object().shape({
  name: yup
    .string()
    .min(7, 'O nome deve ter pelo menos 7 caracteres')
    .max(50, 'O nome deve ter no maximo 50 caracteres')
    .required('O nome deve ser informado'),
  email: yup.string().email().required('O e-mail deve ser informado'),
  message: yup
    .string()
    .min(10, 'A mensagem deve ter ao menos 10 caracteres')
    .max(200, 'A mensagem deve ter no maximo 200 caracteres')
    .required('A mensagem deve ser informada'),
});
