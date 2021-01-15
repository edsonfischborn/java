import React, { useState, useEffect } from 'react';

// Pages / Components
import Card from './Card';
import { Cards, LoaderCt, PdfViewer } from './styles';
import {
  FaArrowLeft,
  FaMapMarkerAlt,
  FaUser,
  FaBox,
  FaFileAlt,
} from 'react-icons/fa';
import { Link } from 'react-router-dom';
import { Document, Page, pdfjs } from 'react-pdf';
import Layout from '../_layouts/Panel';
import { toast } from 'react-toastify';
import Loader from '../../components/Loader';

// Services
import api from '../../services/api';
import history from '../../services/history';

// Util
import formatDate from '../../util/formatDate';
pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;

export default function PanelDetail({ match }) {
  const [delivery, setDelivery] = useState({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function getDelivery() {
      try {
        const [, , , id] = window.location.pathname.split('/');

        const { data } = await api.get(`/delivery/${id}`);

        setDelivery({
          ...data,
          createdAt: formatDate(data.createdAt),
        });
      } catch ({ response = {} }) {
        if (response.status && response.status >= 500) {
          toast.error('Erro interno. Tente mais tarde');
        }

        history.push('/painel');
      } finally {
        setLoading(false);
      }
    }

    getDelivery();
  }, []);

  return (
    <Layout>
      <header>
        <h2>
          Detalhes da Entrega{' '}
          <span style={{ color: '#7a7d82', fontSize: '0.8em' }}>
            {`#${String(delivery.id || '0').padStart(3, '0')}`}
          </span>
        </h2>
        <Link to="/painel">
          <button>
            <FaArrowLeft /> Voltar
          </button>
        </Link>
      </header>
      {loading ? (
        <LoaderCt>
          <Loader type="BallTriangle" />
        </LoaderCt>
      ) : (
        <div>
          <Cards>
            <Card icon={<FaBox />} title="Encomenda">
              <div>
                <strong>Status</strong>
                <p>{delivery.deliveryState.stateName}</p>
              </div>

              <div>
                <strong>Apresentada em</strong>
                <p>{delivery.createdAt}</p>
              </div>

              <div>
                <strong>Descrição</strong>
                <p>{delivery.description}</p>
              </div>
            </Card>

            <Card icon={<FaUser />} title="Destinatario">
              <div>
                <strong>Nome</strong>
                <p>{delivery.receiverName}</p>
              </div>
              <div>
                <strong>CPF</strong>
                <p>{delivery.receiverCpf}</p>
              </div>
              <div>
                <strong>Formas de contato</strong>
                <p>{delivery.receiverContact}</p>
              </div>
            </Card>

            <Card icon={<FaMapMarkerAlt />} title="Endereço de entrega">
              <div>
                <strong>Rua</strong>
                <p>{delivery.street}</p>
              </div>

              <div>
                <strong>Número</strong>
                <p>{delivery.number}</p>
              </div>

              <div>
                <strong>CEP</strong>
                <p>{delivery.zipCode}</p>
              </div>

              <div>
                <strong>Cidade</strong>
                <p>{delivery.city}</p>
              </div>

              <div>
                <strong>Bairro</strong>
                <p>{delivery.description}</p>
              </div>
              <div>
                <strong>Estado</strong>
                <p>{delivery.state}</p>
              </div>
              <div>
                <strong>Complemento</strong>
                <p>{delivery.complement}</p>
              </div>
            </Card>
          </Cards>

          <PdfViewer>
            <h3>
              <FaFileAlt />
              Declaração de recebimento
            </h3>
            {!delivery.receiverSignature ? (
              <span>(Disponível após ser entregue)</span>
            ) : (
              <Document
                file={`data:application/pdf;base64,${delivery.receiverSignature}`}
              >
                <Page pageNumber={1} />
              </Document>
            )}
          </PdfViewer>
        </div>
      )}
    </Layout>
  );
}
