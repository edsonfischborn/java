import React, { useState } from 'react';

// Components
import { Input } from '@rocketseat/unform';
import { toast } from 'react-toastify';
import { Form } from './styles';
import Loader from '../../../components/Loader';

// Services
import api from '../../../services/api';

// Schema
import schema from './schema';

export default function SiteMessageForm() {
  const [loading, setLoading] = useState(false);

  async function handleSubmit(data, { resetForm }) {
    try {
      setLoading(true);
      await api.post('/message', data);

      resetForm({});
      toast.success('Mensagem enviada com sucesso! agurde nosso retorno');
    } catch ({ response = {} }) {
      if (response.status && response.status === 400) {
        toast.error('Falha, verifique os campos e tente novamente!');
      } else {
        toast.error('Erro interno, tente mais tarde!');
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <Form schema={schema} onSubmit={handleSubmit}>
      <h4>Deixe sua mensagem</h4>

      <label htmlFor="name">Nome</label>
      <Input name="name" type="text" id="name" placeholder="Nome completo" />

      <label htmlFor="email">E-mail</label>
      <Input
        name="email"
        type="email"
        id="email"
        placeholder="usuario@dominio.com"
      />

      <label htmlFor="message">Mensagem</label>
      <Input
        multiline
        name="message"
        id="message"
        placeholder="Digite sua mensagem aqui"
      />

      <button type="submit">
        {loading ? <Loader type="ThreeDots" /> : 'Enviar'}
      </button>
    </Form>
  );
}
