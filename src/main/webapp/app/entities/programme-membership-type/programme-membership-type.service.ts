import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, BaseRequestOptions} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {ProgrammeMembershipType} from "./programme-membership-type.model";
@Injectable()
export class ProgrammeMembershipTypeService {

	private resourceUrl = 'api/programme-membership-types';

	constructor(private http: Http) {
	}

	create(programmeMembershipType: ProgrammeMembershipType): Observable<ProgrammeMembershipType> {
		let copy: ProgrammeMembershipType = Object.assign({}, programmeMembershipType);
		return this.http.post(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	update(programmeMembershipType: ProgrammeMembershipType): Observable<ProgrammeMembershipType> {
		let copy: ProgrammeMembershipType = Object.assign({}, programmeMembershipType);
		return this.http.put(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	find(id: number): Observable<ProgrammeMembershipType> {
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
