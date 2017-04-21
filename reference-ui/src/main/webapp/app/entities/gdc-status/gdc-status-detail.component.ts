import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {GdcStatus} from "./gdc-status.model";
import {GdcStatusService} from "./gdc-status.service";

@Component({
	selector: 'jhi-gdc-status-detail',
	templateUrl: './gdc-status-detail.component.html'
})
export class GdcStatusDetailComponent implements OnInit, OnDestroy {

	gdcStatus: GdcStatus;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private gdcStatusService: GdcStatusService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['gdcStatus']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.gdcStatusService.find(id).subscribe(gdcStatus => {
			this.gdcStatus = gdcStatus;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
