package com.obsoft.inci2019

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.obsoft.inci2019.models.CartStore
import com.obsoft.inci2019.models.Item
import com.obsoft.inci2019.models.ItemsStore

class ItemsGridFragment: Fragment(), BaseFragment {
    private val CALLERID = 1
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter:ItemsAdapter
    private lateinit var itemsList:List<Item>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.activity_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        itemsList = ItemsStore.getList(context)
        recyclerView = view.findViewById<RecyclerView>(R.id.cartItemsGrid)
        var recyclerLayout = GridLayoutManager(context, 2)
        recyclerAdapter = ItemsAdapter(itemsList)

        recyclerView!!.apply {
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }

        configureReceiver()

    }
    override public fun registerReceiver() : Int { return CALLERID }
    override public fun handleBroadcast(p0: Context?, p1: Intent?) {
        if (p1!!.action == ItemsStore.ItemsLoadedAction ) {
            itemsList = ItemsStore.getList()
            recyclerAdapter!!.updateData(itemsList)
        } else if (p1!!.action == CartStore.Action.ItemAdded.actionName) {
            Toast.makeText(p0, "Item Added", Toast.LENGTH_LONG).show()
        }
    }
    private fun configureReceiver() {
        val filter = IntentFilter()
        filter.addAction(ItemsStore.ItemsLoadedAction)
        filter.addAction(CartStore.Action.ItemAdded.actionName)
        val receiver = ItemsGridBroadcastReceiver()
        context?.let { LocalBroadcastManager.getInstance(it).registerReceiver(receiver, filter) }
//        registerReceiver(receiver, filter)
    }

    inner class ItemsGridBroadcastReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            handleBroadcast(p0, p1)
        }
    }
}