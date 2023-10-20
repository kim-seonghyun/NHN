# 1. 영화 '퍼스트 맨'의 제작 연도, 영문 제목, 러닝 타임, 플롯을 출력하세요.
select ReleaseDateInKorea, Title, RunningTime, Plot from movie where KoreanTitle = '퍼스트 맨';

# 2. 2003년에 개봉한 영화의 한글 제목과 영문 제목을 출력하세요
select KoreanTitle, Title from movie where ReleaseYear=2003;


# 3. 영화 '글래디에이터'의 작곡가를 고르세요
select Name
from person
where PersonID IN (SELECT appear.PersonID
                  from appear
                  where RoleID IN (SELECT RoleID from role where RoleKorName = '작곡')
                    AND MovieID IN (select MovieID from movie where KoreanTitle = '글래디에이터'));


# 4. 영화 '매트릭스' 의 감독이 몇명인지 출력하세요

select COUNT(Name)
from person
where PersonID IN (SELECT appear.PersonID
                  from appear
                  where RoleID IN (SELECT RoleID from role where RoleKorName = '감독')
                    AND MovieID IN (select MovieID from movie where KoreanTitle = '매트릭스'));

# 5. 감독이 2명 이상인 영화를 출력하세요

select Title
from movie
where MovieID IN
      (select MovieID from appear where RoleID IN (SELECT RoleID from role where RoleKorName = '감독') group by MovieID HAVING COUNT(MovieID) >= 2);

# 6. '한스 짐머'가 참여한 영화 중 아카데미를 수상한 영화를 출력하세요

select Title
from movie
where MovieID IN (select MovieID
                  from appear
                  where PersonID IN (SELECT PersonID from person where KoreanName = '한스 짐머')
                    and AppearID IN (select AppearID
                                     from awardinvolve where WinningID IN (2)));






# 7. 감독이 '제임스 카메론'이고 '아놀드 슈워제네거'가 출연한 영화를 출력하세요

select Title
from movie
where MovieID IN (SELECT MovieID
                  from appear
                  where PersonID IN
                        (select PersonID from person where KoreanName IN ('제임스 카메론') OR KoreanName IN ('아놀드 슈워제네거')));

# 8. 상영시간이 100분 이상인 영화 중 레오나르도 디카프리오가 출연한 영화를 고르시오

select Title
from movie
where RunningTime >= 100
  AND MovieID IN
      (SELECT MovieID from appear where PersonID IN (SELECT PersonID from person where KoreanName IN ('레오나르도 디카프리오')));

# 9. 청소년 관람불가 등급의 영화 중 가장 많은 수익을 얻은 영화를 고르시오

Select Title from movie where GradeInKoreaID =4 ORDER BY (BoxOfficeWWGross + BoxOfficeUSGross) DESC LIMIT 1;


# 10. 1999년 이전에 제작된 영화의 수익 평균을 고르시오

select avg(BoxOfficeUSGross + BoxOfficeWWGross) AS 영화수익평균
from movie where ReleaseYear <=1999;

# 11. 가장 많은 제작비가 투입된 영화를 고르시오.

select Title
from movie
order by Budget desc limit 1;

# 12. 제작한 영화의 제작비 총합이 가장 높은 감독은 누구입니까?

select person.KoreanName, SUM(movie.Budget) AS 예산
from appear
join movie ON appear.MovieID = movie.MovieID
join person on appear.PersonID = person.PersonID
where RoleID = 2
group by appear.PersonID order by 예산 desc limit 1;


# 13. 출연한 영화의 모든 수익을 합하여, 총 수입이 가장 많은 배우를 출력하세요.

select person.KoreanName, SUM(movie.BoxOfficeWWGross + movie.BoxOfficeUSGross) AS 수익
from appear
join movie ON appear.MovieID = movie.MovieID
join person on appear.PersonID = person.PersonID
where RoleID = 6 || appear.RoleID = 7
group by appear.PersonID order by 수익 desc limit 1;

# 14. 제작비가 가장 적게 투입된 영화의 수익을 고르세요. (제작비가 0인 영화는 제외합니다)

select Title, (BoxOfficeWWGross + BoxOfficeUSGross) AS 수익
from movie
where Budget > 0
order by Budget
limit 1;

# 15. 제작비가 5000만 달러 이하인 영화의 미국내 평균 수익을 고르세요

select AVG(BoxOfficeUSGross) AS 미국내평균수익
from movie
where Budget <= 500000000;


# 16. 액션 장르 영화의 평균 수익을 고르세요

select AVG(BoxOfficeUSGross + movie.BoxOfficeWWGross) AS 액션장르영화평균수익
from movie
         join moviegenre on moviegenre.MovieID = movie.MovieID
where GenreID = (select GenreID from genre where GenreKorName = '액션');


# 17. 드라마, 전쟁 장르의 영화를 고르세요.

select Title
from movie
         join moviegenre on moviegenre.MovieID = movie.MovieID
where GenreID = (select GenreID from genre where GenreKorName = '액션') || GenreID = (select  GenreID from genre where GenreKorName='전쟁');

# 18. 톰 행크스가 출연한 영화 중 상영 시간이 가장 긴 영화의 제목, 한글제목, 개봉연도를 출력하세요.

select Title, KoreanTitle, ReleaseYear
from movie
where MovieID IN (select MovieID from appear where PersonID IN (select PersonID from person where KoreanName = '톰 행크스'))
ORDER BY RunningTime desc
limit 1;

# 19. 아카데미 남우주연상을 가장 많이 수상한 배우를 고르시오

select person.Name, count(awardinvolve.SectorID) AS 수상개수
from awardinvolve
         join appear on awardinvolve.AppearID = appear.AppearID
         join person on person.PersonID = appear.PersonID
where awardinvolve.SectorID IN (SELECT SectorID from sector where SectorKorName IN ('남우주연상'))
AND WinningID = (SELECT winning.WinningID from winning where WinOrNot = 'Winner') group by person.Name order by 수상개수 desc limit 1;

# 20. 아카데미상을 가장 많이 수상한 배우를 고르시오 ('수상자 없음'이 이름인 영화인은 제외합니다)

select Name AS 아카데미상을가장많이수상한영화인
from person
         join appear on person.personID = appear.PersonID
where AppearID IN (select AppearID
                   from awardinvolve
                   where WinningID IN (SELECT WinningID from winning where WinOrNot = 'Winner'))
  AND KoreanName <> '수상자 없음'
  AND RoleID IN (SELECT RoleID from role where RoleKorName = '배우')
group by appear.PersonID
order by COUNT(appear.PersonID) desc
limit 1;

# 21. 아카데미 남우주연상을 2번 이상 수상한 배우를 고르시오

select person.Name, count(awardinvolve.SectorID) AS 아카데미남우주연상수상개수
from awardinvolve
         join appear on awardinvolve.AppearID = appear.AppearID
         join person on person.PersonID = appear.PersonID
where awardinvolve.SectorID IN (SELECT SectorID from sector where SectorKorName IN ('남우주연상'))

  AND WinningID = (SELECT winning.WinningID from winning where WinOrNot = 'Winner')
group by person.Name
HAVING 아카데미남우주연상수상개수 >= 2
order by 아카데미남우주연상수상개수 desc;



# 23. 아카데미상을 가장 많이 수상한 영화인을 고르시오 ('수상자 없음'이 이름인 영화인은 제외합니다)


select Name AS 아카데미상을가장많이수상한사람
from person
         join appear on person.personID = appear.PersonID
where AppearID IN (select AppearID
                   from awardinvolve
                   where WinningID IN (SELECT WinningID from winning where WinOrNot = 'Winner'))
  AND KoreanName <> '수상자 없음'
group by appear.PersonID
order by COUNT(appear.PersonID) desc limit 1;



# 24. 아카데미상에 가장 많이 노미네이트 된 영화를 고르세요.

select Title AS 아카데미상에가장많이노미네이트된영화
from movie
         join appear on movie.MovieID = appear.MovieID
where AppearID IN (select AppearID
                   from awardinvolve
                   where WinningID IN (SELECT WinningID from winning where WinOrNot = 'Nominated'))
group by appear.MovieID
order by COUNT(appear.MovieID) desc limit 1;

# 25. 가장 많은 영화에 출연한 여배우를 고르세요.

select name
from person
where PersonID = (select PersonID
                  from appear
                  where RoleID IN (select RoleID from role where RoleName = 'Actress')
                  group by PersonID
                  order by Count(PersonID) desc
                  limit 1);


# 26. 수익이 가장 높은 영화 TOP 10을 출력하세요.

select Title, (BoxOfficeWWGross + BoxOfficeUSGross) AS 수익
from movie
order by 수익 desc
limit 10;

# 27. 수익이 10억불 이상인 영화중 제작비가 1억불 이하인 영화를 고르시오.
select Title, (BoxOfficeWWGross + BoxOfficeUSGross) AS 수익
from movie
where  (BoxOfficeWWGross + BoxOfficeUSGross) >= 1000000000 AND Budget <= 100000000;


# 28. 전쟁 영화를 가장 많이 감독한 사람을 고르세요.

select Name AS 전쟁영화를가장많이감독한사람
from person
where PersonID = (SELECT PersonID
                   from appear
                   where MovieID IN (SELECT MovieID
                                     from moviegenre
                                     where GenreID IN (select GenreID from genre where GenreKorName = '전쟁')
                                       AND RoleID IN (select RoleID from role where RoleKorName = '감독'))
                   group by PersonID
                   order by COUNT(PersonID) desc limit 1);


# 29. 드라마에 가장 많이 출연한 사람을 고르세요.

select Name AS 드라마에가장많이출연한사람
from person
where PersonID = (select PersonID
                  from appear
                  where MovieID in (select MovieID
                                    from moviegenre
                                    where GenreID in (select GenreID from genre where GenreKorName = '드라마'))
                    and RoleID IN (select RoleID
                                   from role
                                   where RoleKorName = '배우'
                                      or RoleKorName = '잡역부'
                                      or RoleKorName = '스턴트')
                  group by PersonID
                  order by COUNT(PersonID) desc
                  limit 1);


# 30. 드라마 장르에 출연했지만 호러 영화에 한번도 출연하지 않은 사람을 고르세요.

select Name
from person
where PersonID in (select PersonID
                   from appear
                            join moviegenre on appear.MovieID = moviegenre.MovieID
                   where moviegenre.GenreID IN (select GenreID from genre where GenreKorName = '드라마'))
  AND PersonID not in (select PersonID
                       from appear
                                join moviegenre on appear.MovieID = moviegenre.MovieID
                       where moviegenre.GenreID IN (SELECT GenreID from genre where GenreKorName = '호러'));


# 31. 아카데미 영화제가 가장 많이 열린 장소는 어디인가요?

select Location
from awardyear
group by Location
order by COUNT(awardyear.Location) desc
limit 1;

# 33. 첫 번째 아카데미 영화제가 열린지 올해 기준으로 몇년이 지났나요?
select year(now()) - Year
from awardyear
order by Year
limit 1;



