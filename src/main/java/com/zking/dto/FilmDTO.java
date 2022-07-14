package com.zking.dto;

import com.zking.entity.Film;
import lombok.Data;

import java.util.Date;

@Data
public class FilmDTO {
    private Integer id;
    private String name;
    private String mp4Src;
    private String info;
    private String imgSrc;
    private String actor;
    private String director;
    private Integer vip;
    private Date time;
    private Double score;
    private Integer heat;
    private String region;
    private String types;

    public static FilmDTO getFilmDTO(Film film, String type){
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(film.getId());
        filmDTO.setName(film.getName());
        filmDTO.setMp4Src(film.getMp4Src());
        filmDTO.setInfo(film.getInfo());
        filmDTO.setImgSrc(film.getImgSrc());
        filmDTO.setActor(film.getActor());
        filmDTO.setDirector(film.getDirector());
        filmDTO.setVip(film.getVip());
        filmDTO.setTime(film.getTime());
        filmDTO.setScore(film.getScore());
        filmDTO.setHeat(film.getHeat());
        filmDTO.setRegion(film.getRegion());
        filmDTO.setTypes(type);
        return filmDTO;
    }


}
