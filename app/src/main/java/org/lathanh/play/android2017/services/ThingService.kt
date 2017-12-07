package org.lathanh.play.android2017.services

/**
 * Created by rlathanh on 2017-10-03.
 */

class ThingService {

    //== Public inner classes ===================================================

    class Thing(val id: Long, val name: String)


    //== API methods ============================================================

    fun getThing(id: Long): Thing? {
        try {
            Thread.sleep(LOAD_DELAY_MS)
        } catch (e: InterruptedException) {
            // who dares interrupt my sleep?!
        }

        if (id < 1 || id >= ALL_THE_THINGS.size) return null

        return ALL_THE_THINGS[id.toInt()]
    }

    companion object {

        //== Private constants ======================================================

        private val LOAD_DELAY_MS: Long = 750

        private val ALL_THE_THINGS = arrayOfNulls<Thing>(5)

        init {
            ALL_THE_THINGS[1] = Thing(1, "Sticks")
            ALL_THE_THINGS[2] = Thing(2, "Rocks")
            ALL_THE_THINGS[3] = Thing(3, "Clocks")
            ALL_THE_THINGS[4] = Thing(4, "Gum")
        }
    }
}
