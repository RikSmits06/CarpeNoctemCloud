alter table category
    add column svg_path text default '/img/file.svg';

update category
set matching_extensions = '{"mkv", "mp4", "ogv", "avi", "mov", "wmv", "mpeg", "mpg", "m4v"}',
    svg_path='/img/film.svg'
where name = 'Film';

update category
set matching_extensions = '{"mp3", "wav", "webm", "ogg", "au"}',
    svg_path='/img/music.svg'
where name = 'Audio';

update category
set matching_extensions = '{"jpeg", "gif", "png", "jpg", "svg", "webp", "bmp"}',
    svg_path='/img/image.svg'
where name = 'Pictures';
