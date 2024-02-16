package se.miun.anby2001.dt031g.bathingsites.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import se.miun.anby2001.dt031g.bathingsites.R
import se.miun.anby2001.dt031g.bathingsites.database.BathingSite
import se.miun.anby2001.dt031g.bathingsites.databinding.ItemBathingSiteBinding

/**
 * Adapter for recyclerView with a clickListener to react on clicks on bathing sites
 */
class BathingSiteAdapter(private val bathSites: List<BathingSite>,
                            private val clickListener: (BathingSite) -> Unit):
                                RecyclerView.Adapter<BathingSiteAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View, clickAtPosition: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemBathingSiteBinding.bind(itemView)

        // click listener on position of item
        init {
            itemView.setOnClickListener {
                clickAtPosition(adapterPosition)
            }
        }

        // data binding for storing the information of bathing site
        fun databind(bathSite: BathingSite) {
            binding.tvName.text = bathSite.name
            binding.tvAddress.text = bathSite.address
            binding.tvLatitude.text = bathSite.latitude.toString()
            binding.tvLongitude.text = bathSite.longitude.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_bathing_site, parent, false)
        ) {
            clickListener(bathSites[it])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(bathSites[position])
    }

    override fun getItemCount(): Int {
        return bathSites.size
    }
}