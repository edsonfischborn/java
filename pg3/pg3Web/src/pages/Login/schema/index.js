import * as yup from 'yup';

export default yup.object().shape({
  email: yup.string().email().required('O e-mail deve ser informado'),
  password: yup
    .string()
    .min(6, 'A senha deve ter pelo menos 6 caracteres')
    .max(15, 'A senha deve ter no maximo 15 caracteres')
    .required('A senha deve ser informada'),
});
