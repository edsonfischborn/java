import * as yup from 'yup';

export default yup.object().shape({
  receiverName: yup
    .string()
    .min(7, 'O nome deve ter no minimo 7 caracteres')
    .max(50, 'O nome deve ter no maximo 50 caracteres')
    .required('O nome deve ser informado'),
  receiverCpf: yup
    .number()
    .transform((value) => {
      if (isNaN(value)) return undefined;
      else return String(value).length === 11 ? value : undefined;
    })
    .required('O cpf deve conter 11 números'),
  receiverContact: yup
    .string()
    .min(15, 'As informações de contato devem ter no minimo 15 caracteres')
    .max(150, 'As informações de contato devem ter no maximo 50 caracteres')
    .required('As informações de contato devem ser informadas'),
  street: yup
    .string()
    .min(3, 'O nome da rua devem ter no minimo 3 caracteres')
    .max(50, 'O nome da rua devem ter no maximo 50 caracteres')
    .required('O nome da rua deve ser informado'),
  complement: yup
    .string()
    .max(150, 'O complemento deve ter no maximo 150 caracteres'),
  number: yup
    .string()
    .min(1, 'O número deve ter no minimo 1 caractere')
    .max(10, 'O número deve ter no maximo 10 caracteres'),
  district: yup
    .string()
    .min(3, 'O bairro deve ter no minimo 3 caracteres')
    .max(80, 'O bairro deve ter no maximo 80 caracteres')
    .required('O bairro deve ser informado'),
  zipCode: yup
    .string()
    .min(5, 'O Cep deve ter no minimo 5 caracteres')
    .max(20, 'O Cep deve ter no maximo 20 caracteres')
    .required('O Cep deve ser informado'),
  description: yup
    .string()
    .min(10, 'O descrição deve ter no minimo 10 caracteres')
    .max(200, 'O descrição deve ter no maximo 200 caracteres')
    .required('A descrição deve ser informada'),
  city: yup.string().required('A cidade deve ser informada'),
  state: yup.string().required('O estado deve ser informado'),
});
