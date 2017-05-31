package com.keller23.cards

import com.badlogic.ashley.core.*
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.google.inject.*

class GameMain : ApplicationAdapter() {
    internal lateinit var batch: SpriteBatch
    internal lateinit var img: Texture
    internal lateinit var c13: Texture
    /*internal lateinit var s13: Texture
    internal lateinit var h13: Texture
    internal lateinit var d13: Texture
    internal lateinit var c12: Texture
    internal lateinit var s12: Texture
    internal lateinit var h12: Texture
    internal lateinit var d12: Texture*/
    internal val engine = Engine()
    private lateinit var injector: Injector
    internal var deck1: Deck = Deck()

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
        c13 = Texture("playingCards/1.jpg")
        /*s13 = Texture("playingCards/2.jpg")
        h13 = Texture("playingCards/3.jpg")
        d13 = Texture("playingCards/4.jpg")
        c12 = Texture("playingCards/5.jpg")
        s12 = Texture("playingCards/6.jpg")
        h12 = Texture("playingCards/7.jpg")
        d12= Texture("playingCards/8.jpg")*/
        injector = Guice.createInjector(GameModule(this))
        injector.getInstance(Systems::class.java).list.map{injector.getInstance(it)}.forEach { system ->
            engine.addSystem(system)
        }

        deck1.insertJoker = false
        deck1.aceHigh = true
        deck1.init()

        createEntities()
    }

    private fun createEntities() {
        /*engine.addEntity(Entity().apply {
            add(TextureComponent(img))
            add(TransformComponent(Vector2(0F,0F)))
        })*/
        //val spacingX: Float = 60F

        val spacingX: Float = 1.3F
        val baseX: Float = Gdx.graphics.width.pixelsToMeters - (spacingX * 4F) - (spacingX / 3.5F)
        val zeroizeY: Float = 3.1F

        /*for (i in 0..4) engine.addEntity(Entity().apply {
            add(TextureComponent(Texture("playingCards/" + (i+1).toString() + ".jpg")))
            //add(TransformComponent(Vector2((baseX + (i * spacingX)), 0F)))
            add(TransformComponent(Vector2(0F + (i * spacingX), zeroizeY)))
        })*/

        var i: Int = 0
        for (card in deck1.cards) {
            if (card.value == Value.AceLow || card.value == Value.AceHigh) {
                card.transformComponent = TransformComponent(Vector2(0F + (i * spacingX), zeroizeY))
                engine.addEntity(Entity().apply {
                    add(card.textureComponent)
                    add(card.transformComponent)
                })
                i += 1
            }
        }


        /*engine.addEntity(Entity().apply {
            add(TextureComponent(c13))
            add(TransformComponent(Vector2(0F,1F)))
        })*/

        /*engine.addEntity(Entity().apply {
            add(TextureComponent(c13))
            add(TransformComponent(Vector2(baseX,0F)))
        })
        engine.addEntity(Entity().apply {
            add(TextureComponent(s13))
            add(TransformComponent(Vector2(75F,0F)))
        })
        engine.addEntity(Entity().apply {
            add(TextureComponent(h13))
            add(TransformComponent(Vector2(150F,0F)))
        })
        engine.addEntity(Entity().apply {
            add(TextureComponent(c12))
            add(TransformComponent(Vector2(225F,0F)))
        })
        engine.addEntity(Entity().apply {
            add(TextureComponent(s12))
            add(TransformComponent(Vector2(0F,210F)))
        })*/
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, .502f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        engine.update(Gdx.graphics.deltaTime)
        /*batch.begin()
        batch.draw(img, 0f, 0f)
        batch.end()*/
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }
}

class RenderingSystem @Inject constructor(private val batch: SpriteBatch,
                                          private val camera: OrthographicCamera):
        IteratingSystem(Family.all(TransformComponent::class.java, TextureComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val img =  entity.texture.texture
        val position = entity.transform.position
        batch.draw(img, position.x, position.y, img.width.pixelsToMeters, img.height.pixelsToMeters)
    }

    override fun update(deltaTime: Float) {
        batch.projectionMatrix = camera.combined
        batch.begin()
        super.update(deltaTime)
        batch.end()
    }

    companion object {
        val PIXELS_PER_METER = 32F
    }
}

/*class SpamSystem @Inject constructor(private val spriteBatch: SpriteBatch): EntitySystem() {
    override fun update(deltaTime: Float) {
        println(deltaTime.toString() + "; " + spriteBatch)
    }
}*/

val Int.pixelsToMeters: Float
    get() = this / 32F

class GameModule(private val gameMain: GameMain) : Module {
    override fun configure(binder: Binder) {
        binder.bind(SpriteBatch::class.java).toInstance(gameMain.batch)
    }

    @Provides @Singleton
    fun systems(): Systems {
        return Systems(listOf(
            //SpamSystem::class.java
            RenderingSystem::class.java
        ))
    }
    
    @Provides @Singleton
    fun camera(): OrthographicCamera {
        /*val viewportWidth = Gdx.graphics.width.pixelsToMeters
        val viewportHeight = Gdx.graphics.height.pixelsToMeters*/
        val viewportWidth = Gdx.graphics.width.pixelsToMeters
        val viewportHeight = Gdx.graphics.height.pixelsToMeters
        return OrthographicCamera(viewportWidth, viewportHeight).apply {
            position.set(viewportWidth / 2F, viewportWidth / 2F, 0F)
            update()
        }
    }
}

data class Systems(val list: List<Class<out EntitySystem>>) {}