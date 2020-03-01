import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import SockJS from "sockjs-client"
import { Stomp } from '@stomp/stompjs';

const useStyles = makeStyles(theme => ({
  formControl: {
    margin: theme.spacing(3),
  },
}));

export default function Match(props) {
  const classes = useStyles();
  const [myHand, setMyHand] = React.useState('');
  const [result, setResult] = React.useState({result: null, otherHand: ''});
  const [connected, setConnected] = React.useState(false);

  const handleHand = event => {
    const hand = event.target.value;
    setMyHand(hand);
    fetch(`http://localhost:8080/match/${props.matchId}/${props.playerId}/${hand}`, {method: 'POST'});
  };

  React.useEffect(() => {
    if(connected) {
      return;
    }

    const socket = new SockJS('http://localhost:8080/websocket');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
      setConnected(true);
      stompClient.subscribe('/topic/' + props.matchId, function(message) {
        const dto = JSON.parse(message.body);

        if(!dto.winner) {
          var result = 'DRAW';
        }
        else if(dto.winner == props.playerId) {
          var result = 'WIN';
        }
        else {
          var result = 'LOSE';
        }

        setResult({
          result: result,
          hands: dto.hands
        })
      });
    });
  });

  function getOtherPlayersHand(hands, myHand) {
    if(!hands) {
      return null;
    }

    return hands[0] != myHand ? hands[0] : hands[1];
  }

  return !connected ? null : (
    <div>
      <FormControl component="fieldset" className={classes.formControl}>
        <FormLabel component="legend">Me</FormLabel>
        <RadioGroup name="player1" value={myHand} onChange={handleHand}>
          <FormControlLabel disabled={myHand != ''} value="ROCK" control={<Radio />} label="Rock" />
          <FormControlLabel disabled={myHand != ''} value="PAPER" control={<Radio />} label="Paper" />
          <FormControlLabel disabled={myHand != ''} value="SCISSORS" control={<Radio />} label="Scissors" />
        </RadioGroup>
      </FormControl>
      <FormControl component="fieldset" className={classes.formControl}>
        <FormLabel component="legend">Other player</FormLabel>
        <RadioGroup name="player1" value={getOtherPlayersHand(result.hands, myHand)}>
          <FormControlLabel disabled value="ROCK" control={<Radio />} label="Rock" />
          <FormControlLabel disabled value="PAPER" control={<Radio />} label="Paper" />
          <FormControlLabel disabled value="SCISSORS" control={<Radio />} label="Scissors" />
        </RadioGroup>
      </FormControl>
      {result.result ? <h1>{result.result}</h1> : null}
    </div>
  );
}