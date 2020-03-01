import React from 'react';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import { Client, Message } from '@stomp/stompjs';

import { makeStyles } from '@material-ui/core/styles';
import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';

const useStyles = makeStyles(theme => ({
  formControl: {
    margin: theme.spacing(3),
  },
}));

function RadioButtonsGroup(props) {
  const classes = useStyles();
  const [myHand, setMyHand] = React.useState('');
  const [otherHand, setOtherHand] = React.useState('otherHand');

  console.log(myHand);
  const handleHand = event => {
    setMyHand(event.target.value);
  };

  var disabled = true;
  return (
    <div>
      <FormControl component="fieldset" className={classes.formControl}>
        <FormLabel component="legend">Me</FormLabel>
        <RadioGroup aria-label="gender" name="player1" value={myHand} onChange={handleHand}>
          <FormControlLabel disabled={myHand != ''} value="ROCK" control={<Radio />} label="Rock" />
          <FormControlLabel disabled={myHand != ''} value="PAPER" control={<Radio />} label="Paper" />
          <FormControlLabel disabled={myHand != ''} value="SCISSORS" control={<Radio />} label="Scissors" />
        </RadioGroup>
      </FormControl>
      <FormControl component="fieldset" className={classes.formControl}>
        <FormLabel component="legend">Other player</FormLabel>
        <RadioGroup aria-label="gender" name="player1" value={otherHand}>
          <FormControlLabel disabled value="ROCK" control={<Radio />} label="Rock" />
          <FormControlLabel disabled value="PAPER" control={<Radio />} label="Paper" />
          <FormControlLabel disabled value="SCISSORS" control={<Radio />} label="Scissors" />
        </RadioGroup>
      </FormControl>
    </div>
  );
}

function App() {
  const [matchId, setMatchId] = React.useState('');
  function keyPress(e) {
    if(e.keyCode == 13) {
      setMatchId(e.target.value);
    }
  }
  return (
    <div>
      <TextField id="match-id" label="Match ID" disabled={matchId != ''} onKeyDown={keyPress}/>
      <FormHelperText>Enter Match ID and press return to create or join a match</FormHelperText>
      {matchId == '' ? null : <RadioButtonsGroup matchId={matchId}/>}
    </div>
  );
}

export default App;
