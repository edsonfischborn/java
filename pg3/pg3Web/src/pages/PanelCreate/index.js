import React, { useState, useEffect } from 'react';

// Pages / Components
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { Input, Select } from '@rocketseat/unform';
import { FaArrowLeft, FaUserAlt, FaMapMarkerAlt, FaBox } from 'react-icons/fa';
import { FormGroup, MultiData, Form, FormSubmit } from './styles';
import Loader from '../../components/Loader';
import Layout from '../_layouts/Panel';

// Services
import api from '../../services/api';
import UserStorage from '../../services/UserStorage';

// Schema
import schema from './schema';

export default function PanelCreate() {
  const [cities, setCities] = useState([]);
  const [states, setStates] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    async function fillData() {
      const states = await getStates();
      setStates(states);
    }

    fillData();
  }, []);

  async function getStates() {
    try {
      const { data } = await api.get(
        'https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome'
      );

      return data.map((state) => ({
        title: state.nome,
        sigla: state.sigla,
        id: state.id,
      }));
    } catch (ex) {
      toast.error('Falha ao carregar a lista de estados');
      return null;
    }
  }

  async function getCities(uf) {
    try {
      const { data } = await api.get(
        `https://servicodados.ibge.gov.br/api/v1/localidades/estados/${uf}/distritos?orderBy=nome`
      );

      return data.map((city) => ({
        title: city.nome,
        id: city.id,
      }));
    } catch (ex) {
      toast.error('Falha ao carregar a lista de cidades');
      return null;
    }
  }

  async function handleSelectedUf(e) {
    const cities = await getCities(e.target.value);
    setCities(cities);
  }

  async function handleSubmit(data, { resetForm }) {
    try {
      setLoading(true);

      const delivery = {
        ...data,
        city: cities.find((city) => city.id === Number(data.city)).title,
        state: states.find((state) => state.id === Number(data.state)).sigla,
        userId: UserStorage.get().id,
      };

      await api.post('/delivery', delivery);

      toast.success('Encomenda cadastrada com sucesso');
      resetForm({});
    } catch ({ response = {} }) {
      if (response.status && response.status === 400) {
        toast.error(
          'Falha no cadastro, verifique os campos e tente novamente!'
        );
      } else {
        toast.error('Erro interno, tente mais tarde!');
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <Layout>
      <header>
        <h2>Cadastro de entrega</h2>
        <Link to="/painel">
          <button>
            <FaArrowLeft /> Voltar
          </button>
        </Link>
      </header>

      <Form onSubmit={handleSubmit} schema={schema}>
        <FormGroup>
          <h4>
            <FaUserAlt /> Dados do destinatario
          </h4>
          <MultiData>
            <div>
              <label htmlFor="receiverName">Nome</label>
              <Input
                name="receiverName"
                type="text"
                id="receiverName"
                placeholder="Ex: Pedro da silva"
              />
            </div>
            <div>
              <label htmlFor="receiverCpf">CPF</label>
              <Input
                name="receiverCpf"
                type="text"
                id="receiverCpf"
                placeholder="Ex: 00000000000"
              />
            </div>
          </MultiData>
          <MultiData>
            <div>
              <label htmlFor="receiverContact">Formas de contato</label>
              <Input
                name="receiverContact"
                type="text"
                id="receiverContact"
                placeholder="Ex: Telefone: 51999548654, e-mail: geraldo@dominLengthio.com"
              />
            </div>
          </MultiData>
        </FormGroup>
        <FormGroup>
          <h4>
            <FaMapMarkerAlt /> Endereço de entrega
          </h4>
          <MultiData>
            <div>
              <label htmlFor="street">Rua</label>
              <Input
                name="street"
                type="text"
                id="street"
                placeholder="Ex: Rua Parque dos coqueiros"
              />
            </div>
            <div>
              <label htmlFor="complement">Complemento</label>
              <Input
                name="complement"
                id="complement"
                type="text"
                placeholder="Ex: prox posto de saúde"
              />
            </div>
          </MultiData>
          <MultiData>
            <div>
              <label htmlFor="number">Número</label>
              <Input
                name="number"
                id="number"
                type="text"
                placeholder="Ex: 239"
              />
            </div>
            <div>
              <label htmlFor="district">Bairro</label>
              <Input
                name="district"
                id="district"
                type="text"
                placeholder="Ex: Morada Campo Novo II"
              />
            </div>
            <div>
              <label htmlFor="zipCode">CEP</label>
              <Input
                name="zipCode"
                id="zipCode"
                type="text"
                placeholder="Ex: 94820-411"
              />
            </div>
          </MultiData>
          <MultiData>
            <div>
              <label htmlFor="state">Estado</label>
              <Select
                placeholder="Selecione..."
                name="state"
                id="state"
                onChange={(e) => handleSelectedUf(e)}
                options={states}
              />
            </div>
            <div>
              <label htmlFor="city">Cidade</label>
              <Select
                placeholder="Selecione..."
                id="city"
                name="city"
                options={cities}
              />
            </div>
          </MultiData>
        </FormGroup>
        <FormGroup>
          <h4>
            <FaBox /> Dados da encomenda
          </h4>
          <MultiData>
            <div>
              <label htmlFor="description">
                Descreva as caracteristicas da embalagem: altura, largura,
                fragilidade, etc...
              </label>
              <Input
                name="description"
                id="description"
                type="text"
                rows="5"
                multiline
                placeholder="Ex: Caixa de madeira, L: 90cm A: 50cm. Item fragil, cuidado ao transportar!"
              ></Input>
            </div>
          </MultiData>
        </FormGroup>
        <FormSubmit>
          <button type="submit">
            {loading ? <Loader type="ThreeDots" /> : 'Cadastrar'}
          </button>
        </FormSubmit>
      </Form>
    </Layout>
  );
}
