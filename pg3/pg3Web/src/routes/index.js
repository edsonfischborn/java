import React from 'react';
import { Switch } from 'react-router-dom';
import Route from './Route';

// Pages
import Home from '../pages/Home';
import Login from '../pages/Login';
import Register from '../pages/Register';
import Panel from '../pages/Panel';
import PanelDetail from '../pages/PanelDetail';
import PanelCreate from '../pages/PanelCreate';
import NotFound from '../pages/NotFound';

export default class Routes extends React.Component {
  render() {
    return (
      <Switch>
        <Route path="/" exact component={Home} />
        <Route path="/login" component={Login} />
        <Route path="/registrar" component={Register} />

        <Route path="/painel" exact auth component={Panel} />
        <Route path="/painel/detalhes/:id" auth component={PanelDetail} />
        <Route path="/painel/cadastrar" auth component={PanelCreate} />

        <Route path="*" component={NotFound} />
      </Switch>
    );
  }
}
