package com.zking.dto;

import com.zking.entity.Film;
import com.zking.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmDto {
    private Integer id;
    private String name;
    private String filmFile;
    private String info;
    private String filmImg;
    private String actor;
    private String director;
    private Integer vip;
    private Date time;
    private Double score;
    private String filmRegion;
    private List<String> filmType;

    public static FilmDto filmDto(Film film, String img, String file, List<String> filmFile, String filmRegion) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setFilmFile(file);
        filmDto.setInfo(film.getInfo());
        filmDto.setFilmImg(file);
        filmDto.setActor(film.getActor());
        filmDto.setDirector(film.getDirector());
        filmDto.setVip(film.getVip());
        filmDto.setTime(film.getTime());
        filmDto.setScore(film.getScore());
        filmDto.setFilmType(filmFile);
        filmDto.setFilmRegion(filmRegion);
        return filmDto;
    }

    public static List<FilmDto> filmDtos(List<Film> films,
                                         List<String> files,
                                         List<String> imgs,
                                         List<String> filmRegions,
                                         List<List<String>> fileTypes
                                         ) {
        List<FilmDto> list = new ArrayList<>();
        for (Film film : films){
            FilmDto filmDto = new FilmDto();
            filmDto.setId(film.getId());
            filmDto.setName(film.getName());
            filmDto.setInfo(film.getInfo());
            filmDto.setActor(film.getActor());
            filmDto.setDirector(film.getDirector());
            filmDto.setVip(film.getVip());
            filmDto.setTime(film.getTime());
            filmDto.setScore(film.getScore());
            list.add(filmDto);
        }
        int count = 0;
        for (String file : files){
            list.get(count).setFilmFile(file);
            count++;
        }
        count = 0;
        for (String img : imgs){
            list.get(count).setFilmImg(img);
            count++;
        }
        count = 0;
        for (String regions : filmRegions){
            list.get(count).setFilmRegion(regions);
            count++;
        }
        count = 0;
        for (List<String> types : fileTypes){
            list.get(count).setFilmType(types);
            count++;
        }
        return list;
    }
}
