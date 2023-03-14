package com.example.sesliasistan.assistant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sesliasistan.R


class AssistantAdapter(private val listeKonusma:ArrayList<Konusma>):RecyclerView.Adapter<AssistantAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssistantAdapter.ViewHolder {

        val v=LayoutInflater.from(parent.context).inflate(R.layout.assistant_item_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AssistantAdapter.ViewHolder, position: Int) {
        val item=listeKonusma[position]
        holder.asistan.text=item.asistan
        holder.human.text=item.human
    }

    override fun getItemCount(): Int {

        return listeKonusma.size
    }
inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var asistan:TextView
    var human:TextView
    init {
        asistan=itemView.findViewById(R.id.asistanText)
        human=itemView.findViewById(R.id.humanText)
    }
}

}