import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {DBC} from "./dbc.model";
import {DBCService} from "./dbc.service";

@Component({
	selector: 'jhi-dbc-detail',
	templateUrl: './dbc-detail.component.html'
})
export class DBCDetailComponent implements OnInit, OnDestroy {

	dBC: DBC;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private dBCService: DBCService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['dBC']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.dBCService.find(id).subscribe(dBC => {
			this.dBC = dBC;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
