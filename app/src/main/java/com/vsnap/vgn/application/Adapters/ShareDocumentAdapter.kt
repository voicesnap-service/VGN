package com.vsnap.vgn.application.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.vsnap.vgn.application.Interface.CallStatusListener
import com.vsnap.vgn.application.Interface.ShareDocumentCheckListener
import com.vsnap.vgn.application.Modal.GetShareDocumentData
import com.vsnap.vgn.application.R

class ShareDocumentAdapter constructor(
    data: List<GetShareDocumentData>, context: Context,
    val checkListener: ShareDocumentCheckListener
) :
    RecyclerView.Adapter<ShareDocumentAdapter.MyViewHolder>() {
    var documentlist: List<GetShareDocumentData> = ArrayList()
    var context: Context
    var Position: Int = 0

    companion object {
        var checkClick: ShareDocumentCheckListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.share_document_list_ui, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data: GetShareDocumentData = documentlist.get(position)
        Position = holder.getAbsoluteAdapterPosition()

        checkClick = checkListener
        holder.lblDocumentName!!.setText(data.document_name)

        holder.lblDocumentName!!.setOnClickListener(View.OnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(data.file_url)
            )
            context.startActivity(browserIntent)
        })

        holder.chbox!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkClick?.add(data)
            } else {
                checkClick?.remove(data)
            }
        })
    }

    override fun getItemCount(): Int {
        return documentlist.size
    }

    inner class MyViewHolder constructor(itemView: View?) : RecyclerView.ViewHolder(
        (itemView)!!
    ) {
        @JvmField
        @BindView(R.id.lblDocumentName)
        var lblDocumentName: TextView? = null

        @JvmField
        @BindView(R.id.chbox)
        var chbox: CheckBox? = null

        init {
            ButterKnife.bind(this, (itemView)!!)
        }
    }

    init {
        documentlist = data
        this.context = context
    }
}