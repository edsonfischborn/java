import React, { useState } from 'react';

// Components / Pages
import { Form, Input } from '@rocketseat/unform';
import { FaSignInAlt, FaUserPlus } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import Auth from '../_layouts/Auth';
import Loader from '../../components/Loader';
import { toast } from 'react-toastify';

// Assets
import bg from '../../assets/c-item-1.jpg';

// Services
import api from '../../services/api';
import history from '../../services/history';

// Schema
import schema from './schema';

// Config
import { colorsConfig } from '../../config/appConfig';

export default function SignUp() {
  const [loading, setLoading] = useState(false);

  async function handleSubmit(formData) {
    try {
      setLoading(true);

      const { status } = await api.post('/user', formData);

      if (status === 201) {
        history.push('/login');
      } else {
        throw new Error();
      }
    } catch ({ response = {} }) {
      if (response.status && response.status >= 400 && response.status < 500) {
        toast.error('Informações invalidas!, revise as informações!');
      } else {
        toast.error('Erro interno, tente mais tarde!');
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <Auth bgImg={bg}>
      <Form schema={schema} onSubmit={handleSubmit}>
        <h2>
          <FaUserPlus /> Registre-se
        </h2>

        <label htmlFor="name">Nome</label>
        <Input name="name" id="name" type="name" placeholder="Nome Completo" />

        <label htmlFor="email">E-mail</label>
        <Input
          name="email"
          id="email"
          type="email"
          placeholder="email@dominio.com"
        />

        <label htmlFor="phone">Celular</label>
        <Input name="phone" id="phone" type="phone" placeholder="51999999999" />

        <label htmlFor="cpf">CPF</label>
        <Input name="cpf" id="cpf" type="cpf" placeholder="00000000000" />

        <label htmlFor="password">Senha (6-15 caracteres)</label>
        <Input name="password" id="password" type="password" />

        <button type="submit">
          {loading ? (
            <Loader color={colorsConfig.textLight} type="ThreeDots" />
          ) : (
            <>
              Registre-se <FaSignInAlt />
            </>
          )}
        </button>

        <p>
          Já tem uma conta? <Link to="/login">Clique-aqui</Link> e faça seu
          login
        </p>
      </Form>
    </Auth>
  );
}
