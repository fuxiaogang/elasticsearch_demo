if(p_location_id && p_location_id > 0){
    if(doc['location'] && p_lat && p_lon){
        def v_distance = doc['location'].distance(p_lat,p_lon);
        if(p_location_id == doc[p_location_name].value && doc['cat'].value == '店铺'){
            return v_distance <= p_distance ? 80.0 + _score : 80;
        }else if(p_location_id == doc[p_location_name].value && doc['cat'].value == '商品'){
            return v_distance <= p_distance ? 50.0 + _score : 50;
        }else{
            return v_distance <= p_distance ? 20.0 + _score : 20;
        }
    }else{
        return 1;
    }
}else{
    return 1;
}