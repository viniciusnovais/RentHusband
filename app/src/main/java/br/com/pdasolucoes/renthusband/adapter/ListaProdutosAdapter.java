package br.com.pdasolucoes.renthusband.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.pdasolucoes.renthusband.R;
import br.com.pdasolucoes.renthusband.model.Ferramenta;

/**
 * Created by PDA on 08/06/2017.
 */

public class ListaProdutosAdapter extends RecyclerView.Adapter<ListaProdutosAdapter.MyViewHolder> {

    private List<Ferramenta> lista;
    private Context context;

    public ListaProdutosAdapter(List<Ferramenta> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list_produto, null);
        MyViewHolder mvh = new MyViewHolder(layoutView);

        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tvNomeProduto.setText(lista.get(position).getNome());

        holder.tvDescricao.setText(lista.get(position).getDescricao());

        byte[] decodedString = Base64.decode(lista.get(position).getFoto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imageView.setImageBitmap(decodedByte);

    }


    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNomeProduto,tvDescricao;
        public ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvNomeProduto = (TextView) itemView.findViewById(R.id.tvNomeProduto);
            tvDescricao = (TextView) itemView.findViewById(R.id.tvDescricao);
            imageView = (ImageView) itemView.findViewById(R.id.imageFerramenta);
        }
    }
}
