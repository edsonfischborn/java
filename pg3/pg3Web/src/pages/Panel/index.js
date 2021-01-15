import React, { useState, useEffect } from 'react';

// Pages / Components
import { FaEye, FaPlus, FaArrowLeft, FaArrowRight } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import Badge from '../../components/Badge';
import Loader from '../../components/Loader';
import Layout from '../_layouts/Panel';
import { TableCt, Table, Badges, HandlePage, InfoContainer } from './styles';

// Services
import api from '../../services/api';

export default function Panel() {
  const [deliveries, setDeliveries] = useState([]);
  const [user, setUser] = useState({});
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [lastPage, setLastPage] = useState(false);
  const [tableRows] = useState(5);
  const [tableRowsAnimate, setTableRowsAnimate] = useState(0);

  useEffect(() => {
    setTableRowsAnimate(
      setTimeout(() => setTableRowsAnimate(0), tableRows * 700)
    );
  }, [page, tableRows]);

  // Get user data
  useEffect(() => {
    try {
      const user = JSON.parse(localStorage.getItem('user'));
      setUser(user);
    } catch (ex) {
      toast.error('Erro ao carregar os dados do usuário');
    }
  }, []);

  // Get user deliveries
  useEffect(() => {
    async function fillData() {
      try {
        if (!user.id) return;

        const { data } = await api(`/delivery?size=${tableRows}`, {
          params: {
            userId: user.id,
            page,
          },
        });

        setDeliveries(
          data.content.map((data) => {
            const name = data.receiverName.split(' ');

            return {
              ...data,
              receiverName: `${name[0]} ${name[name.length - 1]}`,
            };
          })
        );

        setLastPage(data.last);
        setLoading(false);
      } catch (ex) {
        toast.error('Erro ao carregar as entregas');
      }
    }

    fillData();
  }, [user, page, tableRows]);

  function handlePagination(action = 'next') {
    if (page === 0 && action === 'previous') return;

    const newPage = action === 'next' ? page + 1 : page - 1;
    setPage(newPage);
    setLoading(true);
  }

  return (
    <Layout>
      <header>
        <h2>Suas entregas</h2>
        <Badges>
          <div>
            <Badge color="#22bb33" />
            <span>Entregue</span>
          </div>
          <div>
            <Badge color="#f0ad4e" />
            <span>Pendente</span>
          </div>
          <div>
            <Badge color="#bb2124" />
            <span>Problema na entrega</span>
          </div>
        </Badges>
        <Link to="/painel/cadastrar">
          <button>
            <FaPlus />
            Cadastrar
          </button>
        </Link>
      </header>

      <TableCt>
        <Table>
          <thead>
            <tr>
              <th>Cod</th>
              <th>Destinatário</th>
              <th>Cidade</th>
              <th>Estado</th>
              <th>Status</th>
              <th>Detalhes</th>
            </tr>
          </thead>
          <tbody>
            {loading
              ? null
              : deliveries.map((delivery, i) => {
                  return (
                    <tr
                      className={
                        tableRowsAnimate !== 0
                          ? `animate__animated animate__bounceInRight animate__delay-${i}s`
                          : ''
                      }
                      key={delivery.id}
                    >
                      <td>{`#${String(delivery.id).padStart(3, '0')}`}</td>
                      <td>{delivery.receiverName}</td>
                      <td>{delivery.city}</td>
                      <td>{delivery.state}</td>
                      <td>
                        <Badge color={delivery.deliveryState.stateColor} />
                      </td>
                      <td>
                        <Link to={`/painel/detalhes/${delivery.id}`}>
                          <FaEye />
                        </Link>
                      </td>
                    </tr>
                  );
                })}
          </tbody>
        </Table>

        {loading ? (
          <InfoContainer>
            {loading ? <Loader type="BallTriangle" /> : null}

            {deliveries.length === 0 && loading === false ? (
              <span>Você ainda não cadastrou encomendas</span>
            ) : null}
          </InfoContainer>
        ) : null}
      </TableCt>

      {deliveries.length > 0 && tableRowsAnimate === 0 ? (
        <HandlePage>
          {page === 0 ? null : (
            <button onClick={() => handlePagination('previous')}>
              <FaArrowLeft />
              <span>anterior</span>
            </button>
          )}

          {!lastPage ? (
            <button onClick={() => handlePagination('next')}>
              <span>proxíma</span>
              <FaArrowRight onClick={() => handlePagination('next')} />
            </button>
          ) : null}
        </HandlePage>
      ) : null}
    </Layout>
  );
}
