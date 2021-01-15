import React, { useState } from 'react';

// Components / Pages
import { Form, Input } from '@rocketseat/unform';
import { FaSignInAlt, FaLock } from 'react-icons/fa';
import { toast } from 'react-toastify';
import { Link } from 'react-router-dom';
import Loader from '../../components/Loader';
import Auth from '../_layouts/Auth';

// Assets
import bg from '../../assets/bg-login.jpg';

// Services
import api from '../../services/api';
import history from '../../services/history';
import UserStorage from '../../services/UserStorage';
import TokenStorage from '../../services/TokenStorage';

// Schema
import schema from './schema';

// Config
import { colorsConfig } from '../../config/appConfig';

// Other
import LoginError from './LoginError';

export default function Login() {
  const [loading, setLoading] = useState(false);

  async function handleSubmit({ email, password }) {
    try {
      setLoading(true);

      const token = await getToken(email, password);
      TokenStorage.store(`Bearer ${token}`);

      const user = await getUser();
      UserStorage.store(user);

      history.push('/painel');
    } catch ({ message, data = {} }) {
      switch (message) {
        case 'getToken': {
          const { status } = data;

          if (status && status >= 400 && status < 500) {
            toast.error('Flaha na autenticação, verifique os dados informados');
          }
          break;
        }
        case 'getUser': {
          toast.error('Erro ao carregar os dados do usuário. Tente mais tarde');
          break;
        }
        default: {
          toast.error('Erro interno. Tente mais tarde');
        }
      }

      TokenStorage.clear();
      UserStorage.clear();
    } finally {
      setLoading(false);
    }
  }

  async function getToken(email, password) {
    try {
      const { data } = await api.post('/login', {
        username: email,
        password,
      });

      return data.token;
    } catch ({ response = {} }) {
      throw new LoginError('getToken', response);
    }
  }

  async function getUser() {
    try {
      const { jti: id } = TokenStorage.getData();
      api.defaults.headers.Authorization = TokenStorage.get();

      const { data } = await api.get(`/user/${id}`);

      return data;
    } catch ({ response = {} }) {
      throw new LoginError('getUser', response);
    }
  }

  return (
    <Auth bgImg={bg}>
      <Form schema={schema} onSubmit={handleSubmit}>
        <h2>
          <FaLock /> Login
        </h2>

        <label htmlFor="email">E-mail</label>
        <Input
          name="email"
          id="email"
          type="email"
          placeholder="examplo@dominio.com"
        />
        <label htmlFor="password">Senha</label>
        <Input
          name="password"
          id="password"
          type="password"
          autoComplete="on"
        />

        <button type="submit">
          {loading ? (
            <Loader color={colorsConfig.textLight} type="ThreeDots" />
          ) : (
            <>
              Entrar <FaSignInAlt />
            </>
          )}
        </button>

        <p>
          Ainda não tem uma conta? <Link to="/registrar">Clique-aqui</Link> e
          faça seu cadastro
        </p>
      </Form>
    </Auth>
  );
}
