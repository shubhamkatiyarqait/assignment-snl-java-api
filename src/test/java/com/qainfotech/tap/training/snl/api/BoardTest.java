package com.qainfotech.tap.training.snl.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BoardTest {

	Board board;

	@BeforeMethod

	public void load() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException,
	GameInProgressException, MaxPlayersReachedExeption {
		board = new Board();
		board.registerPlayer("shubham");
		board.registerPlayer("sumit");


	}


	@Test(expectedExceptions=PlayerExistsException.class)
	public void acheckANameOfPlayerAlreadyExist() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException
	{
		System.out.println(board);
		board.registerPlayer("sumit");
	}
	
	@Test	
	public void Check_Constructor_Board() throws IOException{
		JSONObject  data1 = board.data;
		Board newtest= new Board(board.uuid);
		JSONObject data2 = newtest.data;
		assertNotEquals(data1,data2);

	}


	@Test(expectedExceptions=MaxPlayersReachedExeption.class)
	public void check_maximum_number_Of_Player() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException
	{
		board.registerPlayer("shivam");
		board.registerPlayer("paras");
		board.registerPlayer("varun");
	}

	@Test(expectedExceptions=NoUserWithSuchUUIDException.class)
	public void No_User_With_Such_UUID() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException,
	GameInProgressException, MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException, InvalidTurnException {

		board.deletePlayer(UUID.randomUUID());

	}

	@Test
	public void Check_PlayerDeleted() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException,
	GameInProgressException, MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException, InvalidTurnException {

		UUID uuid= (UUID)board.getData().getJSONArray("players").getJSONObject(0).get("uuid");
		board.deletePlayer(uuid);

		assertThat(board.getData().length()).isEqualTo(3);
	}
	
	

	@Test(expectedExceptions=GameInProgressException.class)
	public void CheckIfGameAlreadyInProgress() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException
	{
		
		board.rollDice((UUID) ((JSONObject) board.getData().getJSONArray("players").get(0)).get("uuid"));
		board.registerPlayer("Ayush");
	}


	@Test(expectedExceptions=InvalidTurnException.class)
	public void CheckInvalidTurn() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException
	{
		board.rollDice((UUID) ((JSONObject) board.getData().getJSONArray("players").get(1)).get("uuid"));
		
	}
	@Test
	public void players_Turn()throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException {
		board.rollDice((UUID) ((JSONObject) board.getData().getJSONArray("players").get(0)).get("uuid"));
		board.rollDice((UUID) ((JSONObject) board.getData().getJSONArray("players").get(1)).get("uuid"));
		assertThat(board.getData().get("turn")).isEqualTo(0);
	}
	
	@Test
	public void Check_New_Position()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException {
		((JSONObject) board.getData().getJSONArray("players").get(0)).put("position", 100);
		Object d = ((JSONObject) board.getData().getJSONArray("players").get(0)).getInt("position");
		UUID uuid = (UUID) ((JSONObject) board.getData().getJSONArray("players").get(0)).get("uuid");
		Object obj = board.rollDice(uuid);
		Object msg = ((JSONObject) obj).get("message");
		if ((int) d >= 100) {
			assertThat(msg.toString()).isEqualTo("Incorrect roll of dice. Player did not move");
}
	}
	
	@Test
	public void rollDice_is_valid() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, JSONException, InvalidTurnException {

		UUID uuid = (UUID) board.getData().getJSONArray("players").getJSONObject(0).get("uuid");
		Integer pos = (Integer) board.data.getJSONArray("players").getJSONObject(0).get("position");
		Integer dice = board.rollDice(uuid).getInt("dice");
		Integer expected_pos = pos+dice;
		Integer target = (Integer) board.data.getJSONArray("steps").getJSONObject(dice).get("target");
		assertTrue(dice<=6);
		Integer type = (Integer) board.data.getJSONArray("steps").getJSONObject(dice).get("type");
		if(type==0)
		{
			assertEquals( expected_pos, target);
		}
		if(type==1)
		{
			assertTrue( expected_pos>target);
		}
		if(type==2)
		{
			assertTrue( expected_pos<target);
		}

	}
	



}
