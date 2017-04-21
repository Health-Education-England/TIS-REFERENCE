import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, BaseRequestOptions} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {CurriculumSubType} from "./curriculum-sub-type.model";
@Injectable()
export class CurriculumSubTypeService {

	private resourceUrl = 'api/curriculum-sub-types';

	constructor(private http: Http) {
	}

	create(curriculumSubType: CurriculumSubType): Observable<CurriculumSubType> {
		let copy: CurriculumSubType = Object.assign({}, curriculumSubType);
		return this.http.post(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	update(curriculumSubType: CurriculumSubType): Observable<CurriculumSubType> {
		let copy: CurriculumSubType = Object.assign({}, curriculumSubType);
		return this.http.put(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	find(id: number): Observable<CurriculumSubType> {
		return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
			return res.json();
		});
	}

	query(req?: any): Observable<Response> {
		let options = this.createRequestOption(req);
		return this.http.get(this.resourceUrl, options)
			;
	}

	delete(id: number): Observable<Response> {
		return this.http.delete(`${this.resourceUrl}/${id}`);
	}


	private createRequestOption(req?: any): BaseRequestOptions {
		let options: BaseRequestOptions = new BaseRequestOptions();
		if (req) {
			let params: URLSearchParams = new URLSearchParams();
			params.set('page', req.page);
			params.set('size', req.size);
			if (req.sort) {
				params.paramsMap.set('sort', req.sort);
			}
			params.set('query', req.query);

			options.search = params;
		}
		return options;
	}
}
