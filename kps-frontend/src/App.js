import React from 'react';
import TextField from '@material-ui/core/TextField';
import FormHelperText from '@material-ui/core/FormHelperText';
import Match from './Match';

const matchIdRegexp = /^[a-zA-Z0-9]+$/;

function App() {
  const [matchId, setMatchId] = React.useState('');
  const [playerId, setPlayerId] = React.useState('');
  const [error, setError] = React.useState('');
  const [matchIdError, setMatchIdError] = React.useState('');

  async function keyPress(e) {
    if(e.keyCode == 13) {
      let matchId = e.target.value;

      if(!matchIdRegexp.test(matchId)) {
        setMatchIdError('Allowed characters: A-Z, a-z, 0-9');
        return;
      }
      else {
        setMatchIdError('');
      }

      setMatchId(matchId);

      let response = await fetch(`http://localhost:8080/match/${matchId}`, {method: 'POST'});

      if(!response.ok) {
        switch(response.status) {
          case 403:
            setError('The match is full.');
            break;
            case 400:
              setError('Bad request. Illegal match ID?');
              break;
            default:
              setError('Unknown error.')
        }
      }
      else {
        let playerId = await response.text();
        setPlayerId(playerId);
      }
    }
  }

  return (
    <div>
      <h2>Rock-Paper-Scissors</h2>
      <TextField error={matchIdError} id="match-id" label="Match ID" disabled={matchId != ''} onKeyDown={keyPress} helperText={matchIdError}/>
      <FormHelperText>Enter Match ID and press return to create or join a match</FormHelperText>
      {playerId == '' ? null : <Match matchId={matchId} playerId={playerId}/>}
      {error == '' ? null : <div>Error: {error}</div>}
    </div>
  );
}

export default App;
