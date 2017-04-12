import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {LocalOffice} from "./local-office.model";
import {LocalOfficeService} from "./local-office.service";

@Component({
	selector: 'jhi-local-office-detail',
	templateUrl: './local-office-detail.component.html'
})
export class LocalOfficeDetailComponent implements OnInit, OnDestroy {

	localOffice: LocalOffice;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private localOfficeService: LocalOfficeService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['localOffice']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.localOfficeService.find(id).subscribe(localOffice => {
			this.localOffice = localOffice;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
