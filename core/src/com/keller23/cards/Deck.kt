package com.keller23.cards

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2

/***
 * @location: 0 = cards,
 *         1..N = Player#
 * @suit:  0 = Clubs,
 *         1 = Spades,
 *         2 = Hearts,
 *         3 = Diamonds
 * @value: 1..13 = 1-10, J, Q, K, A
 */
data class CardOld(var owner: Int = 0, val suit: Int, val value: Int, var dealt: Boolean = false, var texture: Texture)

internal class Deck(/*private val aceHigh: Boolean = true, private val insertJoker: Boolean = false*/) {
    private val texPre: String = "playingCards/"
    private val texSuf: String = ".jpg"
    internal var aceHigh: Boolean = true
    internal var insertJoker: Boolean = true
    private val jokerBlack: String = "53"
    private val jokerRed: String = "54"
    internal var cards: MutableList<Card> = mutableListOf<Card>()

    private val spacingX: Float = 1.3F
    //private val baseX: Float = Gdx.graphics.width.pixelsToMeters - (spacingX * 4F) - (spacingX / 3.5F)
    private val zeroizeY: Float = 2.5F

    /*fun init() {
        for (suit in 0..3) {
            for (facevalue in 1..13) {
                cards.add(CardOld(suit = suit, value = facevalue, textureComponent = Texture(texPre + (facevalue + suit) + texSuf)))
            }
        }

        println("finished init-cards")
    }*/

    internal fun init() {
        println("begin deck init")

        for (suit in 0..3) {
            for (facevalue in 2..13) {
                cards.add(Card(Location.Deck,
                        Suit.fromInt(suit),
                        Value.fromInt(facevalue),
                        TextureComponent(Texture(texPre + (facevalue + suit) + texSuf)), /*Texture(texPre + (facevalue + suit) + texSuf)*/
                        TransformComponent(Vector2())))
            }

            // Add in Aces, depending on if it is high or low
            if (aceHigh) cards.add(Card(Location.Deck, Suit.fromInt(suit), Value.AceHigh,  TextureComponent(Texture(texPre + (1 + suit) + texSuf)), TransformComponent(Vector2())))
            else cards.add(Card(Location.Deck, Suit.fromInt(suit), Value.AceLow, TextureComponent(Texture(texPre + (1 + suit) + texSuf)), TransformComponent(Vector2())))

            // Add in Jokers, if wanted
            if (insertJoker) {
                if (suit == 0 || suit == 1) cards.add(Card(Location.Deck, Suit.fromInt(suit), Value.Joker, TextureComponent(Texture(texPre + (1 + suit) + texSuf)), TransformComponent(Vector2())))
                else if (suit == 2 || suit == 3) cards.add(Card(Location.Deck, Suit.fromInt(suit), Value.Joker, TextureComponent(Texture(texPre + (1 + suit) + texSuf)), TransformComponent(Vector2())))
            }

        }

        println("finish deck init")
    }
}

internal class Card(val location: Location, val suit: Suit, val value: Value, var textureComponent: TextureComponent, var transformComponent: TransformComponent) {
    /*var location: Location = _location
    val suit: Suit = _suit
    val value: Value = _value*/
}

enum class Suit(val index: Int) {
    Clubs(0),
    Spades(1),
    Hearts(2),
    Diamonds(3),
    Joker(-1),
    None(-1);

    companion object {
        private val suitMap = Suit.values().associateBy(Suit::index);
        fun fromInt(type: Int) = suitMap[type]!!
    }
}

enum class Value(val value: Int) {
    Joker(0),
    AceLow(1),
    Two(2),
    Three(3),
    Four(4),
    Five(5),
    Six(6),
    Seven(7),
    Eight(8),
    Nine(9),
    Ten(10),
    Jack(11),
    Queen(12),
    King(13),
    AceHigh(14);

    companion object {
        private val valueMap = Value.values().associateBy(Value::value);
        fun fromInt(type: Int) = valueMap[type]!!
    }
}

enum class Location {
    //Standard
    Deck,
    Player1,
    Player2,
    Player3,
    Player4,

    //Poker
    Flop,
    Turn,
    River,
    Burn

}